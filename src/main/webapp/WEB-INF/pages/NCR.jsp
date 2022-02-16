<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
   <%@taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
    <%@page session="true"%>
    <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
    <%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
    
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>NCR</title>

<link rel="stylesheet" href="https://cdn.datatables.net/responsive/2.2.3/css/responsive.dataTables.css"/>
<link rel="stylesheet" href="https://cdn.datatables.net/1.10.19/css/dataTables.bootstrap4.min.css"/>
<link href="resources/css/mdb.min.css" rel="stylesheet">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">

<link rel="stylesheet" href="https://cdn.datatables.net/fixedheader/3.1.5/css/fixedHeader.dataTables.min.css"/>
<link rel="stylesheet" href="https://cdn.datatables.net/colreorder/1.5.1/css/colReorder.dataTables.min.css"/>

<link rel="stylesheet" href="https://cdn.datatables.net/buttons/1.5.2/css/buttons.dataTables.min.css"/>
<link rel="stylesheet" href="https://cdn.datatables.net/buttons/1.5.2/css/buttons.bootstrap4.min.css"/>
<link rel="stylesheet" href="resources/css/BootIcons.css"/>

<link rel="stylesheet" href="https://cdn.datatables.net/1.10.19/css/jquery.dataTables.min.css"/>
<script src="https://code.jquery.com/jquery-3.3.1.js"></script>
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.4/umd/popper.min.js"></script>
<!-- Bootstrap core JavaScript -->
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js" integrity="sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy" crossorigin="anonymous"></script>
<!-- MDB core JavaScript -->
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/mdbootstrap/4.5.15/js/mdb.min.js"></script>
<script src="https://cdn.datatables.net/1.10.19/js/jquery.dataTables.min.js"></script>
<script src="https://cdn.datatables.net/1.10.19/js/dataTables.bootstrap4.min.js"></script>
<script src="https://cdn.datatables.net/responsive/2.2.3/js/dataTables.responsive.js"></script>

<script src="https://cdn.datatables.net/fixedheader/3.1.5/js/dataTables.fixedHeader.min.js"></script>
<script src="https://cdn.datatables.net/colreorder/1.5.1/js/dataTables.colReorder.min.js"></script>

<script src="https://cdn.datatables.net/buttons/1.5.2/js/dataTables.buttons.min.js"></script>
<script src="https://cdn.datatables.net/buttons/1.5.2/js/buttons.bootstrap4.min.js"></script>

<script src="https://cdn.datatables.net/buttons/1.5.2/js/buttons.colVis.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jszip/3.1.3/jszip.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.1.36/pdfmake.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.1.36/vfs_fonts.js"></script>
<script src="https://cdn.datatables.net/buttons/1.5.2/js/buttons.html5.min.js"></script>
<script src="https://cdn.datatables.net/buttons/1.5.2/js/buttons.print.min.js"></script>
<link href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.6-rc.0/css/select2.min.css" rel="stylesheet" />
<script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.6-rc.0/js/select2.min.js"></script>
<link rel="stylesheet" href="resources/AdvancedQuery/css/query-builder.default.min.css">
<script type="text/javascript" src="resources/AdvancedQuery/js/doT.min.js"></script>
<script type="text/javascript" src="resources/AdvancedQuery/js/jQuery.extendext.min.js"></script>
<script type="text/javascript" src="resources/AdvancedQuery/js/moment.min.js"></script>
<script type="text/javascript" src="resources/AdvancedQuery/js/query-builder.min.js"></script>
<script src="https://querybuilder.js.org/node_modules/sql-parser-mistic/browser/sql-parser.min.js"></script>
</head>
<style>
.stepwizard-step p {
    margin-top: 10px;
}
.stepwizard-row {
    display: table-row;
}
.stepwizard {
    display: table;
    width: 50%;
    position: relative;
}
.stepwizard-step button[disabled] {
    opacity: 1 !important;
    filter: alpha(opacity=100) !important;
}
.stepwizard-row:before {
    top: 14px;
    bottom: 0;
    position: absolute;
    content: " ";
    width: 100%;
    height: 1px;
    background-color: #ccc;
    z-order: 0;
}
.stepwizard-step {
    display: table-cell;
    text-align: center;
    position: relative;
}
.btn-circle {
    width: 30px;
    height: 30px;
    text-align: center;
    padding: 6px 0;
    font-size: 12px;
    line-height: 1.428571429;
    border-radius: 15px;
}

</style>
<script>
$(document).ready(function () {
	  var navListItems = $('div.setup-panel div a'),
	          allWells = $('.setup-content'),
	          allNextBtn = $('.nextBtn');

	  allWells.hide();

	  navListItems.click(function (e) {
	      e.preventDefault();
	      var $target = $($(this).attr('href')),
	              $item = $(this);

	      if (!$item.hasClass('disabled')) {
	          navListItems.removeClass('btn-primary').addClass('btn-default');
	          $item.addClass('btn-primary');
	          allWells.hide();
	          $target.show();
	          $target.find('input:eq(0)').focus();
	      }
	  });

	  allNextBtn.click(function(){
	      var curStep = $(this).closest(".setup-content"),
	          curStepBtn = curStep.attr("id"),
	          nextStepWizard = $('div.setup-panel div a[href="#' + curStepBtn + '"]').parent().next().children("a"),
	          curInputs = curStep.find("input[type='text'],input[type='url']"),
	          isValid = true;

	      $(".form-group").removeClass("has-error");
	      for(var i=0; i<curInputs.length; i++){
	          if (!curInputs[i].validity.valid){
	              isValid = false;
	              $(curInputs[i]).closest(".form-group").addClass("has-error");
	          }
	      }

	      if (isValid)
	          nextStepWizard.removeAttr('disabled').trigger('click');
	  });

	  $('div.setup-panel div a.btn-primary').trigger('click');
	});
</script>
<body>
<jsp:include page="NavBar.jsp">
         <jsp:param name="param1" value="Dashboardli"/>
     </jsp:include>
     <div class="page-content d-flex align-items-stretch"> 
       <jsp:include page="SideNave.jsp">
         <jsp:param name="param1" value="NCRLi"/>
     </jsp:include>
<div class="content-inner">
<div class="card mb-4 wow fadeIn" style="visibility: visible; animation-name: fadeIn;">

                <!--Card content-->
                <div class="card-body d-sm-flex justify-content-between">

                      <form class="d-flex justify-content-center">
                     <img alt="" src="resources/img/Icon.png"/>
                      <h4 class="mb-2 mb-sm-0 pt-1">
                         <span> &nbsp Non-confirmity report (NCR)<br>&nbsp CORRECTIVE ACTION</span>
                         
                      </h4>
                    </form>
                   

                   

                </div>

            </div>
          <div class="container">
  

  
  <form:form role="form" action="saveNCR" method="post"  modelAttribute="schedule">
    <form:hidden path="ID"/>
     <div class="form-row">
          
          <div class="col">
          <div class="form-group">
            <label class="control-label">Department</label>
          <form:select  class="form-control " path="ncr_dept_id" items="${listAuditAreas}" disabled="${schedule.ncr_status == 'CLOSED'}" itemValue="audit_area_id" itemLabel="aUDIT_AREA_NAME" />
          </div>
          </div>
          <div class="col">
          <div class="form-group">
            <label class="control-label">Date  </label>
            <form:input maxlength="100" type="date" class="form-control" placeholder="Date" path="ncr_date" readonly="${schedule.ncr_status == 'CLOSED'}"/>
          </div>
          </div>
          </div>
          <div class="form-row">
          <div class="col">
          <div class="form-group">
            <label class="control-label">Other Source</label>
            <form:input maxlength="100" type="text" path="ncr_source" required="required" class="form-control" placeholder="this can be used for data analysis" readonly="${schedule.ncr_status == 'CLOSED'}"/>
          </div>
          </div>
          <div class="col">
          <div class="form-group">
            <label class="control-label">Procedure ref</label>
        <form:input maxlength="100" type="text" path="ncr_other" required="required" class="form-control" placeholder="(for audit findings only or if applicable)" readonly="${schedule.ncr_status == 'CLOSED'}"/>
          </div>
          </div>
          
      
    </div>
    
 
   
    
    <div class="card">
    <div class="card-header text-white bg-info">
    <a class="text-white" data-toggle="collapse" href="#ExcutiveBody" aria-expanded="false" aria-controls="ExcutiveBody">
    NONCONFORMANCE
  </a>
    </div>
    <div class="card-body" id="ExcutiveBody">
    <form:textarea required="required" path="ncr_desc" class="form-control" placeholder="Describe the nonconfirmity..." readonly="${schedule.ncr_status == 'CLOSED'}"></form:textarea>
   <div class="form-group col-md-6">
            <label class="control-label">Initiator :</label>
        <form:input maxlength="100" path="ncr_initiator" type="text" readonly="true" class="form-control" placeholder="(can be auditor or system admin)" />
          </div>
    </div>
  </div>
  
  <div class="card">
    <div class="card-header text-white bg-info">
     <a class="text-white" data-toggle="collapse" href="#Findings" >
    Root CAUSE (to be completed by the concerned Head of Department)
  </a>
    </div>
    <div class="card-body" id="Findings">
    <form:textarea required="required" path="ncr_root_cause" class="form-control" placeholder="Describe the root cause of the nonconfirmity..." readonly="${schedule.ncr_status == 'CLOSED'}"></form:textarea>
    </div>
  </div>
  <div class="card">
    <div class="card-header text-white bg-info">
       <a class="text-white" data-toggle="collapse" href="#Reviewed" aria-expanded="false" aria-controls="Reviewed">
    CORRECTIVE ACTION (to be completed by the concerned HoD)
  </a>
     </div>
    <div class="card-body" id="Reviewed">
    <form:textarea required="required"  path="ncr_corrective_action" class="form-control" placeholder="(Describe the activity to be carried-out to correct the nonconfirmity)" readonly="${schedule.ncr_status == 'CLOSED'}"></form:textarea>
    <div class="form-group col-md-6">
            <label class="control-label">Agreed Completion Date :</label>
        <form:input maxlength="100" type="Date" path="ncr_completion_date" required="required" class="form-control" placeholder="Agreed Completion Date" readonly="${schedule.ncr_status == 'CLOSED'}"/>
          </div>
    </div>
  </div>
  		
   
    <div class="card">
    <div class="card-header text-white bg-info">
       <a class="text-white" data-toggle="collapse" href="#verification" aria-expanded="false" aria-controls="verification">
    VERIFICATION (to be completed by the system admin)
  </a>
     </div>
    <div class="card-body" id="verification">
    <label class="control-label">Verification of closure of corrective actions</label>
    <form:textarea required="required"  class="form-control" path="ncr_verfication_notes"  placeholder="NOTES..." readonly="${schedule.ncr_status == 'CLOSED'}"></form:textarea>
   
    <div class="form-group col-md-6">
            <label class="control-label">Date :</label>
        <form:input maxlength="100" type="Date" path="ncr_verfication_date" required="required" class="form-control" placeholder="Date" readonly="${schedule.ncr_status == 'CLOSED'}"/>
   
          </div>
                  <form:input maxlength="100" type="hidden" path="ncr_status" required="required" class="form-control" placeholder="Status" />
          
    </div>
    <c:choose>
             <c:when test="${schedule.ncr_status == 'NEW'  || schedule.ID==0 }">
          <form:input class="btn btn-info" path="Operation" type="submit"  value="Save"/>
            </c:when>
            </c:choose>
            
            <c:choose>
             <c:when test="${schedule.ncr_status == 'NEW'  && schedule.ID>0 }">
          <form:input class="btn btn-info" path="Operation" type="submit"  value="Close"/>
            </c:when>
            </c:choose>
            
  </div>
 </form:form>  
</div>


	     

	</div>
	</div>
</body>
</html>