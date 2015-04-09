<!DOCTYPE HTML PUBLIC “-//W3C//DTD HTML 4.0 Transitional//EN”>
<html>
<head>
  <style type="text/css">
       .ups-image {
            border-width: 0px;
            border-color: #ffffff;
            border-style: solid;
            background-position:0% 12%;
            background-repeat:no-repeat;
            background-size:115% 100%;
            }

            .usps-image {
            border-width: 0px;
            border-color: #ffffff;
            border-style: solid;
            background-position:0% 0%;
            background-repeat:no-repeat;
            background-size:100% 100%;
            }
            .rotate {
                    position:relative;/* place the text relateve to whatever tag is devined as absolute */
                    -webkit-transform: rotate(-90deg);
                	 margin:0 auto;
                
               		
            }
          
           
    </style>
</head>
<body>

    %for picking in objects :
          <% setLang(picking.partner_id.lang) %>
          %for image in get_label(picking) :
       		
       		<div>
       			<table>
       				<tr>
       					<td>
       					<img src="data:image/png;base64,${picking.company_id.logo}"  width="150" height="50"/>
       					</td>
       				</tr>
       			</table>
       		</div>
       		<div>
       		<table>
       		<tr>
       			<td>
                       <p style="margin-top: 10px;margin-left: 10px;margin-bottom: 10px;">    
                       	   ${picking.company_id.name}</br>
                           ${picking.company_id.partner_id.street}</br>
                           ${picking.company_id.partner_id.city}
                           ${picking.company_id.partner_id.zip}</br>
                           Phone : ${picking.company_id.partner_id.phone}</br>
                           Email : ${picking.company_id.partner_id.email}
                       </p>
                    </td>
            </tr>  
       		</table>
       		</div>
            <div>
           <center>
                <img src=${str(image.url)} alt="logo"  width="550" height="800" />
                <center>
            </div>
            <table height=200>
                	<tr>
                		<td>
                		</td>
                	</tr>
                </table>  
        <div>
     	<div>
       			<table>
       				<tr>
       					<td>
       					<img src="data:image/png;base64,${picking.company_id.logo}"  width="150" height="50"/>
       					</td>
       				</tr>
       			</table>
       		</div>
       		<table>
       		<tr>
       			<td>
                       <p style="margin-top: 10px;margin-left: 10px;margin-bottom: 10px;">    
                       	   ${picking.company_id.name}</br>
                           ${picking.company_id.partner_id.street}</br>
                           ${picking.company_id.partner_id.city}
                           ${picking.company_id.partner_id.zip}</br>
                           Phone : ${picking.company_id.partner_id.phone}</br>
                           Email : ${picking.company_id.partner_id.email}
                       </p>
                    </td>
            </tr>  
       		</table>
       		</div>
      
            <div style="min-height:170 mm;">
            <center>
            <table width="550" border="1" cellspacing="0" cellpadding="0" class="rotate" style="margin-top:170px;">
                <tr>
                    <td colspan="5" align="center"><font size="5">Packing Slip</font></td>
                    
                </tr>
                <tr>    
                    <td colspan="2">
                       <p style="margin-top: 10px;margin-left: 10px;margin-bottom: 10px;">    
                       	   ${picking.company_id.name}</br>
                           ${picking.company_id.partner_id.street}</br>
                           ${picking.company_id.partner_id.city}</br>
                           ${picking.company_id.partner_id.zip}</br>
                           ${picking.company_id.partner_id.state_id.name}</br>
                           ${picking.company_id.partner_id.country_id.name}
                       </p>
                    </td>                 
                    <td colspan="3">
                    <p style="margin-top: 10px;margin-left: 10px;margin-bottom: 10px;">
                            ${picking.partner_id.name}</br>
                            ${picking.partner_id.street}</br>
                            ${picking.partner_id.city}</br>
                            ${picking.partner_id.zip}</br>
                            ${picking.partner_id.state_id.name}</br>
                            ${picking.partner_id.country_id.name}
                    </p>
                    </td>
                       
                </tr>
                <tr bgcolor="white">
                    <td align="center"><p align="center">Product/Part No.</p></td>
                    <td align="center"><p align="center">Qty</p></td>
                    <td align="center"><p align="center">Ship</p></td>
                    <td align="center"><p align="center">Price</p></td>
                    <td align="center"><p align="center">Extended</p></td>                    
                </tr>
                 %for move in get_move_lines(picking.id, image.tracking_id) :
                 <tr bgcolor="white">
                    <td><p align="center">${move.product_id.name}</p></td>
                    <td><p align="center">${move.product_qty}</p></td>
                    <td><p align="center">${move.prodlot_id.name}</p></td>
                    <td><p align="center">${move.price_unit}</p></td>
                    <td><p align="center">Extended</p></td>
                </tr>
                 %endfor
                </table>
                </div>
          <p style="page-break-after:auto"></p>
    %endfor
%endfor  

</body>
</html>
