$(".answerWrite input[type=submit]").click(addAnswer);

function addAnswer(e) {
  e.preventDefault();
  
  var url = $("form[name=answer]").attr("action");
  console.log(url);
  var queryString = $("form[name=answer]").serialize();
  console.log(queryString);

  $.ajax({
    type : 'post',
    url : url,
    data : queryString,
    dataType : 'json',
    error: onError,
    success : onSuccess
  });
}

/*
…}
answerId:7
contents:"asd"
createdDate:1460603503978
questionId:7
timeFromCreateDate:1460603503978
writer:"admin"
 * */
//public long getTimeFromCreateDate() {
//	return this.createdDate.getTime();
//}

function onSuccess(json, status){

  console.log(json);

  console.log("status :" + status);
  if (status) {
	  var answerId = json.answerId;
	  var questionId = json.questionId;
	  var createdDate = json.createdDate;
	  var contents = json.contents;
	  var writer = json.writer;
	  var answerTemplate = $("#answerTemplate").html();
	  var template = answerTemplate.format(answerId, contents, new Date(answer.createdDate), questionId, createdDate, writer);
	 
	  $(".qna-comment-slipp-articles").prepend(template);	  
  } else {
	  alert(result);
  }
}

function onError(xhr, status) {
  alert("addAnswer error입니다");
  console.log("xhr:"+ xhr);
  console.log("status :" + status);
}

$(".qna-comment").on("click", ".form-delete", deleteAnswer);


///api/qna/deleteAnswer
function deleteAnswer(e) {
  e.preventDefault();

  var deleteBtn = $(this);
  var queryString = deleteBtn.closest("form").serialize();
  console.log("qs : " + queryString);

  $.ajax({
    type: 'post',
    url: "/api/qna/deleteAnswer",
    data: queryString,
    dataType: 'json',
    error: function (xhr, status) {
      alert("deleteAnswer error");
    },
    success: function (json, status) {
      var result = json.result;
      if (result.status) {
        deleteBtn.closest('article').remove();
      }
    }
  });
}

String.prototype.format = function() {
  var args = arguments;
  return this.replace(/{(\d+)}/g, function(match, number) {
    return typeof args[number] != 'undefined'
        ? args[number]
        : match
        ;
  });
};