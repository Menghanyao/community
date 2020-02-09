// 点赞
function likeComment(e) {
    var parentId = e.getAttribute("data-id")
    var currentLikeCount = e.getAttribute("data-likeCount")
    console.log(parentId,currentLikeCount)
    $.ajax({
        type: "POST",
        url: "/comment",
        contentType: "application/json",
        data: JSON.stringify({
            "parentId": parentId,
            "likeCount": currentLikeCount,
            "type": 3
        }),
        success: function (response) {
            console.log(response);
            if (response.code == 200) {
                $("#comment_section").hide();
                window.location.reload();
            } else {
                console.log("failure!! code：", response.code ,"message: ", response.message);
                if (response.code == 2003) {
                    //  未登录
                    var isAccepted = confirm(response.message);
                    if (isAccepted) {
                        window.open("https://github.com/login/oauth/authorize?client_id=ae4e022db2a1918dbc40&redirect_uri=http://localhost:8888/callback&scope=user&state=1")
                        window.localStorage.setItem("closeable", true);
                    }
                } else {
                    alert(response.message);
                }
            }
        },
        dataType: "json"
    })
}

// 提交一级评论
function comment2question() {
    var questionId = $("#question_id").val();
    var content = $("#comment_content").val();
    sendAjax(questionId, 1, content)
}

//  提交二级评论
function comment2comment(e) {
    var parentId = e.getAttribute("data-id")
    var content = $("#input-"+ parentId).val()
    sendAjax(parentId, 2, content)
}

//  工具函数
function sendAjax(parentId, type, content) {
    if (!content) {
        alert("评论得有东西");
        return
    }
    $.ajax({
        type: "POST",
        url: "/comment",
        contentType: "application/json",
        data: JSON.stringify({
            "parentId": parentId,
            "content": content,
            "type": type
        }),
        success: function (response) {
            console.log(response);
            if (response.code == 200) {
                $("#comment_section").hide();
                window.location.reload();
            } else {
                console.log("failure!! code：", response.code ,"message: ", response.message);
                if (response.code == 2003) {
                    //  未登录
                    var isAccepted = confirm(response.message);
                    if (isAccepted) {
                        window.open("https://github.com/login/oauth/authorize?client_id=ae4e022db2a1918dbc40&redirect_uri=http://localhost:8888/callback&scope=user&state=1")
                        window.localStorage.setItem("closeable", true);
                    }
                } else {
                    alert(response.message);
                }
            }
        },
        dataType: "json"
    });
    console.log(questionId, content);
}


// 展开并获取二级评论
function collapseComments(e) {
    var id = e.getAttribute("data-id")
    var comment = $("#comment-"+ id)
    var commentIcon = $("#comment-icon-"+ id)
    if (comment.hasClass("in")) {
        //  折叠二级评论
        comment.removeClass("in")
        commentIcon.removeClass("active")
    } else {
        //  展开二级评论

        var subCommentContainer =  $("#comment-"+ id)
        if (subCommentContainer.children().length == 1) {
            //  判断直接加载还是重新请求数据
            $.getJSON("/comment/"+ id, function (data) {
                $.each(data.data.reverse(), function (index, content) {
                    var mediaLeftElement = $("<div/>", {
                        "class": "media-left"
                    }).append($("<img/>", {
                        "class": "media-object img-rounded",
                        "src": content.user.avatarUrl
                    }));

                    var mediaBodyElement = $("<div/>", {
                        "class": "media-body"
                    }).append($("<h5/>", {
                        "class": "media-heading",
                        "html": content.user.name
                    })).append($("<div/>", {
                        "html": content.content
                    })).append($("<div/>", {
                        "class": "menu"
                    }).append($("<span/>", {
                        "class": "pull-right",
                        "html": moment(content.gmtCreate).format('YYYY-MM-DD HH:mm')
                    })));

                    var mediaElement = $("<div/>", {
                        "class": "media"
                    }).append(mediaLeftElement).append(mediaBodyElement);

                    var commentElement = $("<div/>", {
                        "class": "col-lg-12 col-md-12 col-sm-12 col-xs-12 comments",
                    }).append(mediaElement)
                    subCommentContainer.prepend(commentElement)
                })

                comment.addClass("in")  //  展开
                commentIcon.addClass("active")  //  添加css
            })
        } else {
            comment.addClass("in")  //  展开
            commentIcon.addClass("active")  //  添加css
        }

    }

}

function selectTag(e) {
    var tag = e.getAttribute("data-tag")
    var previous = $("#tag").val()
    if (previous && previous.split(',').indexOf(tag) === -1) {
        $("#tag").val(previous + ',' + tag)
    } else {
        if (previous) {
        } else {
            $("#tag").val(tag)
        }
    }
}

function showSelectTag() {
    $("#select-tag").show()
}