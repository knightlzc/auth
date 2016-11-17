    <%@ page contentType="text/html;charset=UTF-8"%>
    <c:set var="pageSize" value="${pageSize}" />
    <c:set var="totalNum" value="${totalNum}" />
    <script type="text/javascript">
    
    </script>
    <c:if test="${not empty paginationList}" >
         <div class="pages">
            <ul class="pagination">
                <c:if test="${paginationList.get(0)==1}" >
                  <li class="disabled">
                      <a href="javascript:;">«</a>
                  </li>
                </c:if>
                <c:if test="${paginationList.get(0)>1}" >
                  <li >
                      <a href="javascript:javascript:go(${paginationList.get(0)-1});">«</a>
                  </li>
                </c:if>
                
                <c:forEach items="${paginationList}" var="pagination" varStatus="status">
                    <c:if test="${pageNum eq pagination}">
                        <li class="active"><a href="javascript:go(${pageNum});">${pageNum} <span class="sr-only">(current)</span></a></li>
<!--                        <li class="active"><a href="#">2 <span class="sr-only">(current)</span></a></li> -->
                    </c:if>
                    <c:if test="${pageNum ne pagination}">
                        <li><a href="javascript:go(${pagination});">${pagination}</a></li>
                    </c:if>
                </c:forEach>
                <c:if test="${paginationList.get(paginationList.size()-1)*pageSize>=totalNum}" >
                  <li class="disabled">
                      <a href="javascript:;">»</a>
                  </li>
                </c:if>
                <c:if test="${paginationList.get(paginationList.size()-1)*pageSize<totalNum}" >
                  <li>
                      <a href="javascript:go(${paginationList.get(paginationList.size()-1)+1});">»</a>
                  </li>
                </c:if>
                

            </ul>
            <div style="float:left">共${totalPage}页&nbsp;共${totalNum}条&nbsp;第<input style="width:30px" id="go_page" type="text" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" >页&nbsp;<a class="btn btn-primary btn-sm" href="javascript:jump()">go</a></div>
        </div>
    </c:if>
       
        <!-- 分页 end -->
