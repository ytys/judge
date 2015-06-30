<%--
    Document   : newjsp
    Created on : Jun 26, 2015, 16:11:54
    Author     : zhanhb
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" session="false"%>
<pre><%
    out.println(request.getClass());
    ServletRequest r = request;
    while (r instanceof ServletRequestWrapper) {
        r = ((ServletRequestWrapper) r).getRequest();
    }
    out.println(r.getClass());
    String test = request.getParameter("test");
    out.println(test);
    if (test != null) {
        out.println(test.length());
    }
%></pre>
