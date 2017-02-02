ACC.account = {



}

$(document).ready(function ()
{
    $('.attach').click(function(e){
        $('.upload').hide();
        var mediaCode = e.target.getAttribute("data");
        var str = ACC.config.contextPath+'/electronics/en/my-account/loaddata';
        $.ajax({
            url: str,
            data : ({
                mediaCode: mediaCode
            }),
            dataType : 'json',
            type: 'GET',
            success: function (result) {
                $("#mediaCode").val(mediaCode);
                var eTable="<div class='container2'><div class='listen col-md-6'><from method='get' class='attForm' action='pdf'>";
                    eTable += "<div class='row'>";
                    eTable += "<p>Family member: </p>";
                     eTable +='';
                    eTable += "<select name='member' id='member'>";
                    $.each(result, function(i, customer) {
                       eTable += "<option value='"+i+"'>"+customer+"</option>";
                   });
                eTable +="</select>";
                    eTable +="</div><br/><div class='row'><input class='btn btn-primary'  type='submit' value='Attach'/></div></form></div></div>"
                $('.attachForm').append(eTable);

            }

        });
    });



    $('.attachForm').submit(function(e){
        var mediaCode = $("#mediaCode").val();
        var userCode = $("#member :selected").val()
        var str = ACC.config.contextPath+'/electronics/en/my-account/attachFile';
        $.ajax({
            url: str,
            data : ({
                mediaCode: mediaCode,userCode:userCode
            }),
            dataType : 'json',
            type: 'POST',
            success: function (result) {
                alert("File should approved")
                $('.upload').show();
                $('.container2').remove();

            }

        });
        e.preventDefault();
    });
});