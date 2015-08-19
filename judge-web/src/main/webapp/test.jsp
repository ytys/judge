<%--
    Document   : test
    Created on : Jul 26, 2015, 2:35:52 PM
    Author     : zhanhb
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@page contentType="text/html" pageEncoding="UTF-8" session="false"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8"/>
        <title>JSP Page</title>
    </head>
    <body>
        ${test}
        <div>
            <sec:authentication property="principal" />
        </div>

        <c:set var="test" value="&amp;"/>

        <pre>
            <jsp:text>${pageContext}</jsp:text>
            <jsp:text>${tt}</jsp:text>
            <form:form servletRelativeAction="/upload" role="form" enctype="multipart/form-data">
                <input type="file" name="file"/>
                <input type="submit"/>
            </form:form>
        </pre>
        <script>/*<![CDATA[/**/jQuery("<div>").qrcode(location.href).appendTo("body");//]]>;</script>

        <input type="text" style="display: inline-block" />
        <input type="text" style="display: inline-block" />
    </body>
</html>
