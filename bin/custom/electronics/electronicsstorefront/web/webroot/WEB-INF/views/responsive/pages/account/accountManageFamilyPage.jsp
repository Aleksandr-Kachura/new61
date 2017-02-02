<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/responsive/formElement" %>


<div class="col-xs-9">
    <br/>
    <table class="table">
        <thead>
        <tr>
            <th>Name</th>
            <th>Email Address</th>
            <th>Edit</th>
            <th style="text-align: left">Remove</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${customers}" var="customer">
            <tr>
                <td>${customer.name}</td>
                <td>${customer.uid}</td>
                <td>
                    <form action="family?CSRFToken=${CSRFToken}" method="post">
                        <input class="hidden" type="text" name="uid" value="${customer.uid}">
                        <input class="btn-success" type="submit" value="Edit Member">
                    </form>
                </td>
                <td>
                    <form action="removeFamilyMember?CSRFToken=${CSRFToken}" method="post">
                        <input class="hidden" type="text" name="uid" value="${customer.uid}">
                        <input class="btn-danger" type="submit" value="Remove Member">
                    </form>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <c:if test="${not empty nonFamilyMembers}">
        <h4>Result:</h4>
        <table class="table">
            <thead>
            <tr>
                <th>Name</th>
                <th>Email Address</th>
                <th></th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${nonFamilyMembers}" var="nonFamilyMember">
                <tr>
                    <td>${nonFamilyMember.name}</td>
                    <td>${nonFamilyMember.uid}</td>
                    <td>
                        <form action="addFamilyMember?CSRFToken=${CSRFToken}" method="post">
                            <input class="hidden" type="text" name="uid" value="${nonFamilyMember.uid}">
                            <input class="btn-success" type="submit" value="Add to Family">
                        </form>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </c:if>
    <c:if test="${noResult}">
        <p>Sorry. No matches.</p>
    </c:if>

    <c:if test="${not empty familyRegisterForm}">

        <h3>Create family member:</h3>

        <form:form method="post" commandName="familyRegisterForm" action="createFamilyMember">
            <formElement:formSelectBox idKey="register.title"
                                       labelKey="register.title" selectCSSClass="form-control"
                                       path="titleCode" mandatory="true" skipBlank="false"
                                       skipBlankMessageKey="form.select.empty" items="${titles}"/>
            <formElement:formInputBox idKey="register.firstName"
                                      labelKey="register.firstName" path="firstName" inputCSS="form-control"
                                      mandatory="true"/>
            <formElement:formInputBox idKey="register.lastName"
                                      labelKey="register.lastName" path="lastName" inputCSS="form-control"
                                      mandatory="true"/>
            <formElement:formInputBox idKey="register.email"
                                      labelKey="register.email" path="email" inputCSS="form-control"
                                      mandatory="true"/>

            <div class="form-actions clearfix">
                <ycommerce:testId code="register_Register_button">
                    <button type="submit" class="btn btn-default btn-block">
                        <spring:theme code='ADD MEMBER'/>
                    </button>
                </ycommerce:testId>
            </div>
        </form:form>
    </c:if>


    <c:if test="${not empty familyUpdateProfileForm}">

        <h3>Update family member:</h3>

        <form:form method="post" commandName="familyUpdateProfileForm" action="updateFamilyMember">
            <formElement:formSelectBox idKey="register.title"
                                       labelKey="register.title" selectCSSClass="form-control"
                                       path="titleCode" mandatory="true" skipBlank="false"
                                       skipBlankMessageKey="form.select.empty" items="${titles}"/>
            <formElement:formInputBox idKey="register.firstName"
                                      labelKey="register.firstName" path="firstName" inputCSS="form-control"
                                      mandatory="true"/>
            <formElement:formInputBox idKey="register.lastName"
                                      labelKey="register.lastName" path="lastName" inputCSS="form-control"
                                      mandatory="true"/>
            <formElement:formInputBox idKey="register.email"
                                      labelKey="register.email" path="email" inputCSS="form-control"
                                      mandatory="true"/>
            <div class="hidden">
                <formElement:formInputBox idKey="register.uid"
                                          labelKey="register.email" path="uid" inputCSS="form-control"
                                          mandatory="true"/>
            </div>
            <div class="form-actions clearfix">
                <ycommerce:testId code="register_Register_button">
                    <button type="submit" class="btn btn-default btn-block">
                        <spring:theme code='UPDATE'/>
                    </button>
                </ycommerce:testId>
            </div>
        </form:form>
    </c:if>
</div>