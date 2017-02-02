<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/responsive/formElement"%>

<div class="col-xs-9">
	<br/>
	<table class="table">
		<thead>
		<tr>
			<th>Code</th>
			<th>Status</th>
			<th>Download</th>
			<th>Message</th>
			<th>Owner</th>
			<th>Edit</th>
			<th style="text-align: left">Remove</th>
		</tr>
		</thead>
		<tbody>
			<c:forEach items="${user.medias}" var="media">
					<tr>
						<td>${media.code}</td>
						<td>${media.status}</td>
						<td><a href="${media.downloadURL}">download</a></td>
						<td>${media.textMedia}</td>
						<td>${media.owner.name}</td>
						<td>
							<form action="editFile?CSRFToken=${CSRFToken}" method="post">
								<input class="hidden" type="text" name="mediaCode" value="${media.code}">
								<input class="btn-success" type="submit" value="Edit File">
							</form>
						</td>
						<td>
							<form action="deleteFile?CSRFToken=${CSRFToken}" method="post">
								<input class="hidden"  name="mediaCode" value="${media.code}">
								<input class="btn-danger" type="submit" value="Remove File">
							</form>
						</td>
					</tr>
			</c:forEach>
		</tbody>
	</table>
	<c:if test="${not empty file}">
		<span>111111111111</span>
	</c:if>
	<c:if test="${empty mediaCode}">
		<div class="ui-grid-a right">
			<form method="POST" class="upload" action="uploadFile?CSRFToken=${CSRFToken}" enctype="multipart/form-data">
				<div class="form-group">
					Message:
					<textarea type="text" class="form-control" name="textMedia"></textarea>
					Select a file:
					<input type="file" class="form-control" name="file">
					Personal associated with this document:
					<div>
						<select class="form-control" name="member" id="member">
							<option value="mySelf">mySelf</option>
							<c:forEach items="${members}" var="member">
								<option value="${member.uid}">${member.name}</option>
							</c:forEach>
						</select>
					</div>

					<input type="submit" class="btn btn-default" value="Upload"> Press here to upload the file!
				</div>
			</form>
		</div>
	</c:if>

</div>

