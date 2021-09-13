$(function () {
  function setListener() {
  $(".square").hover(function () {
    $(this).children("button").css("display", "block");
    $(this).children(".mask").css("background", "rgba(0,0,0,0.5)")
  }, function () {
      $(this).children("button").css("display", "none");
      $(this).children(".mask").css("background", "rgba(0,0,0,0)")
  })
}
})
