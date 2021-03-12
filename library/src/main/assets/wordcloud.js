var userChosenFontFace =  window.jsinterface.getCloudFont();
var data =  window.jsinterface.getCloudString();
var parentWidth = window.jsinterface.getParentWidth();
var parentHeight = window.jsinterface.getParentHeight();

var t = $.parseJSON(data).map(function(d) {  return {text: d.word, size: d.size, color: d.color};});

var fill = d3.scale.category20();
var layout = d3.layout.cloud()
    .size([parentWidth / window.devicePixelRatio, parentHeight / window.devicePixelRatio * .95 ])
    .words(t)
    .padding(0)
    .rotate(function() { return ~~(Math.random() * 2) * 90; })
    .font(userChosenFontFace)
    .fontSize(function(d) { return d.size; })
    .on("end", draw);
layout.start();
function draw(words) {
  d3.select("body").append("svg")
      .attr("width", layout.size()[0])
      .attr("height", layout.size()[1])
    .append("g")
      .attr("transform", "translate(" + layout.size()[0] / 2 + "," + layout.size()[1] / 2 + ")")
    .selectAll("text")
      .data(words)
    .enter().append("text")
      .style("font-size", function(d) { return d.size + "px"; })
      .style("font-family", userChosenFontFace)
      .style("fill", function(d, i) {
        if(new String(d.color).valueOf() == new String("0").valueOf()){
          console.log(d.color);
           return i ;
           } else{
            return d.color;
            }
      })
      .attr("text-anchor", "middle")
      .attr("transform", function(d) {
        return "translate(" + [d.x, d.y] + ")rotate(" + d.rotate + ")";
      })
      .on("click", function(d) { window.jsinterface.onWordClick(d.text); })
      .text(function(d) { return d.text; });
}