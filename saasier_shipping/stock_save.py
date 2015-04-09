    def create_shipment_simplest(self, cr, uid, ids, context=None):
    
        #Preparing Object which will be used in the function.
        attach_object=self.pool.get('ir.attachment')
        stock_move_object = self.pool.get('stock.move')
        stock_picking_out_object = self.pool.get('stock.picking.out')
        stock_picking_out_label_object = self.pool.get('stock.picking.out.label')
        res_users_object = self.pool.get('res.users')
        res_company_obj = self.pool.get('res.company')
        user_record = res_users_object.browse(cr,uid,uid,context=None)

        #Preparing data container, and testers for the loop.
        warehouse_ids_dict = {}
        stock_picking_out_label_id = False

            ##############################################################################################
            # TECHNICAL LOOP: HERE WE WILL SET A DICTIONARY CONTAINING PACKS, WEIGHTS, AND CORRESPONDING MOVE_IDS
            # AND WE WILL CONTROLE ALL DATA SETTED IN ALL DELIVERY ORDERS SELECTED IN SHIPPING QUEUE, AND THIS TO RAISE ERRORS
            # BEFORE STARTING COMMUNICATION WITH EASYPOST API, AND MAKE IT LIKE AN INITIAL CONTROLE, AFTER THIS LOOP, ALL OTHER EXCEPTIONS
            # WILL BE EASYPOST EXCEPTIONS AND ERRORS, AND THEY WILL BE SAVED IN THE FIELDS "EASYPOST_EXCEPTION".
            #############################################################################################
        
        #Technical loop in delivery orders.
        for picking_out_record in self.browse(cr,uid,ids):
            #CHECK IF THERE IS AN EASYPOST API, or STAMPS, FOR CURRENT COMPANY OF THE DELIVERY ORDER, then configure all services of API.
            if picking_out_record.shipping_carrier=="USPS-STAMPS" and \
                ( not picking_out_record.company_id.stamps_integ_id or not picking_out_record.company_id.stamps_username or not picking_out_record.company_id.stamps_password):
                raise osv.except_osv(_('Message!'),  _("Please enter Integration Details for Stamps inside company !"))
            elif picking_out_record.shipping_carrier=="USPS-STAMPS" :
                integration_id = picking_out_record.company_id.stamps_integ_id
                username = picking_out_record.company_id.stamps_username
                password = picking_out_record.company_id.stamps_password
                stamps_configuration = StampsConfiguration(wsdl="testing",integration_id=integration_id,username=username, password=password)
                stamps_service = StampsService(configuration=stamps_configuration)
            if picking_out_record.shipping_carrier in ("USPS","UPS","FEDEX") and \
                ( not picking_out_record.company_id.easypost_api):
                raise osv.except_osv(_('Message!'),  _("Please enter Shipping Api key inside company !"))
            elif picking_out_record.shipping_carrier!="USPS-STAMPS":
                easypost.api_key = picking_out_record.company_id.easypost_api
                
            for move_record in picking_out_record.move_lines:
                #IF there was already a label created for move, jump it
                # if move_record.carrier_tracking_ref:
                    # continue
                            ##############################################################
                            # Controle data entry in all selected deliveries and all move lines
                            ##############################################################
                string_error_indicator = '\n\n- Concerned Delivery Order is : %s .\n - Concerned Pack is : %s' \
                                                            % (str(picking_out_record.name), str(move_record.tracking_id.name))
                                                            
                if move_record.tracking_id.content_type:
                    if not move_record.product_id.HSTariffNumber:
                        raise osv.except_osv(_('Warning!'),  _('HS Tariff Number is required for products with customs activated.\n -Concerned Product : %s\
                                                                                    %s ' % (move_record.product_id.name,string_error_indicator,)))
                    if not move_record.product_id.list_price: 
                        raise osv.except_osv(_('Warning!'),  _('Sale Price is required for products with customs activated.\n -Concerned Product : %s\
                                                                                    %s ' % (move_record.product_id.name,string_error_indicator,)))
                                                                                    
                if move_record.product_id.list_price==0 and move_record.insured:
                    raise osv.except_osv(_('Warning!'),  _('You cannot insure a box with a product which don\'t have a price list.\
                                                                                %s ' % (string_error_indicator,)))
                
                if move_record.product_id.track_outgoing and not move_record.prodlot_id:    
                    raise osv.except_osv(_('Message!'),  _('Serial Number is required for the following product : %s  !\
                                                                                %s ' % (move_record.product_id.name ,string_error_indicator,)))
                
                if not picking_out_record.shipping_carrier and not move_record.tracking_id.box_setup_id:
                    raise osv.except_osv(_('Message!'),  _('Please, choose one of the predefined Shipping Packing for UPS, USPS or \
                                                                                FEDEX in your delivery order, or select Box Setup inside every move line (pack) of this delivery order  !\
                                                                                %s ' % (string_error_indicator,)))
                                                                                
                #Check if a predefined package is set for the choosed shipping_carrier, else a box_setup should be set, or raise error.
                if picking_out_record.shipping_carrier=="UPS" and not picking_out_record.shipping_packaging_ups and not move_record.tracking_id.box_setup_id:
                    raise osv.except_osv(_('Message!'),  _('Please, choose one of the predefined Shipping Packing for UPS in your delivery order, \
                                                                                                            or select Box Setup inside every move line (pack) of this delivery order  !\
                                                                                                            %s ' % (string_error_indicator,)))
                                                                                                            
                if picking_out_record.shipping_carrier=="USPS" and not picking_out_record.shipping_packaging_usps and not move_record.tracking_id.box_setup_id:
                    raise osv.except_osv(_('Message!'),  _('Please, choose one of the predefined Shipping Packing for USPS in your delivery order, \
                                                                                                            or select Box Setup inside every move line (pack) of this delivery order  !\
                                                                                                            %s ' % (string_error_indicator,)))
                                                                                                            
                if picking_out_record.shipping_carrier=="FEDEX" and not picking_out_record.shipping_packaging_fedex and not move_record.tracking_id.box_setup_id:
                    raise osv.except_osv(_('Message!'),  _('Please, choose one of the predefined Shipping Packing for FEDEX in your delivery order, \
                                                                                                            or select Box Setup inside every move line (pack) of this delivery order  !\
                                                                                                            %s ' % (string_error_indicator,)))
                                                                                                            
                if picking_out_record.shipping_carrier=="USPS-STAMPS" and not picking_out_record.shipping_packaging_usps_stamps and not move_record.tracking_id.box_setup_id:
                    raise osv.except_osv(_('Message!'),  _('Please, choose one of the predefined Shipping Packing for USPS-STAMPS in your delivery order, \
                                                                                                            or select Box Setup inside every move line (pack) of this delivery order  !\
                                                                                                            %s ' % (string_error_indicator,)))
               
                #Check if shipping service is shoosed
                if picking_out_record.shipping_carrier=="UPS" and not picking_out_record.shipping_service_ups:
                    raise osv.except_osv(_('Message!'),  _("Please, choose one of the Shipping Services for UPS in your delivery order !\
                                                                                %s " % (string_error_indicator,)))
                    
                if picking_out_record.shipping_carrier=="USPS" and not picking_out_record.shipping_service_usps:
                    raise osv.except_osv(_('Message!'),  _("Please, choose one of the Shipping Services for USPS in your delivery order !\
                                                                                %s " % (string_error_indicator,)))
                    
                if picking_out_record.shipping_carrier=="FEDEX" and not picking_out_record.shipping_service_fedex:
                    raise osv.except_osv(_('Message!'),  _("Please, choose one of the Shipping Services for FEDEX in your delivery order !\
                                                                               %s " % (string_error_indicator,)))
                    
                if picking_out_record.shipping_carrier=="USPS-STAMPS" and not picking_out_record.shipping_service_usps_stamps:
                    raise osv.except_osv(_('Message!'),  _("Please, choose one of the Shipping Services for USPS-STAMPS in your delivery order !\
                                                                               %s " % (string_error_indicator,)))
                    
                #Check if for the choosed shipping_carrier, if predefined package is set else the box_setup should have all dimisions, else raise error.
                if picking_out_record.shipping_carrier=="UPS" and not picking_out_record.shipping_packaging_ups and (\
                not move_record.tracking_id.box_setup_id.length or not move_record.tracking_id.box_setup_id.width or not\
                move_record.tracking_id.box_setup_id.height or move_record.override_weight==0.0):
                    raise osv.except_osv(_('Message!'),  _("Please, choose one of the predefined Shipping Packing for UPS in your delivery order, \
                                                                                or if you already selected Box Setup inside \
                                                                                every move line (pack) of this delivery order, provid dimensions !\
                                                                                %s " % (string_error_indicator,)))
                                                                                
                if picking_out_record.shipping_carrier=="USPS" and not picking_out_record.shipping_packaging_usps and (\
                not move_record.tracking_id.box_setup_id.length or not move_record.tracking_id.box_setup_id.width or not\
                move_record.tracking_id.box_setup_id.height or move_record.override_weight==0.0):
                    raise osv.except_osv(_('Message!'),  _("Please, choose one of the predefined Shipping Packing for USPS in your delivery order, \
                                                                                or if you already selected Box Setup inside\
                                                                                every move line (pack) of this delivery order, provid dimensions !\
                                                                                %s " % (string_error_indicator,)))
                                                                                
                if picking_out_record.shipping_carrier=="FEDEX" and not picking_out_record.shipping_packaging_fedex and (\
                not move_record.tracking_id.box_setup_id.length or not move_record.tracking_id.box_setup_id.width or not\
                move_record.tracking_id.box_setup_id.height or move_record.override_weight==0.0):
                    raise osv.except_osv(_('Message!'),  _("Please, choose one of the predefined Shipping Packing for FEDEX in your delivery order, \
                                                                                or if you already selected Box Setup inside\
                                                                                every move line (pack) of this delivery order, provid dimensions !\
                                                                                %s " % (string_error_indicator,)))
          
                if picking_out_record.shipping_carrier=="USPS-STAMPS" and ( not picking_out_record.shipping_packaging_usps_stamps or picking_out_record.shipping_packaging_usps_stamps=="Package") \
                and (not move_record.tracking_id.box_setup_id.length or not move_record.tracking_id.box_setup_id.width or not\
                move_record.tracking_id.box_setup_id.height or move_record.override_weight==0.0):
                    raise osv.except_osv(_('Message!'),  _("Please, choose one of the predefined Shipping Packing for USPS-STAMPS in your delivery order, \
                                                                                or if you already selected Box Setup inside\
                                                                                every move line (pack) of this delivery order, provid dimensions !\
                                                                                %s " % (string_error_indicator,)))  
                   
               #Check if Packs are set, and if Shipping carrier is set too.
                if not move_record.tracking_id:
                    raise osv.except_osv(_('Message!'),  _("Please, you should select a Pack for every move line of this delivery order!\
                                                                                %s " % (string_error_indicator,)))

                                                                                
                #Check if addresses are completly set: address of source location (with phone number, its required for from address)
                #and address of the partner selected in delivery order (customer, phone is not required )
                if not picking_out_record.partner_id.name or not picking_out_record.partner_id.street\
                  or not picking_out_record.partner_id.city or not picking_out_record.partner_id.state_id.name\
                  or not picking_out_record.partner_id.zip:
                    raise osv.except_osv(_('Customer informations missing !'),  _("Please be sure to set the required data and informations about your customer:\
                                                                                                                    name, address, city, state and zip code!\
                                                                                                                    %s " % (string_error_indicator,)))
                                                                                                                    
                if not move_record.location_id.partner_id.name or not move_record.location_id.partner_id.street \
                  or not move_record.location_id.partner_id.city or not move_record.location_id.partner_id.state_id.name \
                  or not move_record.location_id.partner_id.zip or not move_record.location_id.partner_id.phone:
                    raise osv.except_osv(_('Source location address informations missing !'),  _("Please be sure to set the required data and informations about your\
                                                                                                                                         source location address: name, address, city, state, zip code and \
                                                                                                                                         Phone Number !\
                                                                                                                                         %s " % (string_error_indicator,)))
                                                                                                                                         
                            ##############################################################
                            #End of control of data entry in all selected deliveries and all move lines
                            ##############################################################
                                          
                                          
                                          
                            ##############################################################
                            # REEL START : Here the reel start of creation of labels, after raising
                            # all kinds of exceptions about data entry in the top loops, now any exception
                            # will be reported in the field easypost_exception in the corresponding delivery order,
                            # or in the corresponding move_lines.
                            ##############################################################
                            
        for picking_out_record in self.browse(cr,uid,ids):
        

            continue_picking_too = False  # Boolean to know if Label data build was successful, if not JUMP ALL DELIVERY LABELS.
            shipment = {}                        #Dict Containing DATA to create Labels for EasyPost.
            pack_dict = {}                       # LIST OF PACKS DATA :
                                                          # pack_dict = {'pack_id' : ['is_label_created','total_weight',[list_of_move_ids]]}
            stamps_tuple = {}                #Dict Containing DATA to create Labels for STAMPS.
            stamps_total_amount_purchase = 0.0
            
            #LOOP IN MOVES AND CREATE pack_dict{}
            for move_record in picking_out_record.move_lines:
                # if move_record.carrier_tracking_ref:
                    # continue
                #Format the dict
                if pack_dict.has_key(str(move_record.tracking_id.id)):
                    pack_dict[str(move_record.tracking_id.id)][1] +=  math.ceil(move_record.override_weight*16)
                    pack_dict[str(move_record.tracking_id.id)][2].append(move_record.id)
                else:
                    pack_dict[str(move_record.tracking_id.id)] = [0,False,[]]
                    pack_dict[str(move_record.tracking_id.id)][0]=False
                    pack_dict[str(move_record.tracking_id.id)][1]= math.ceil(move_record.override_weight*16)
                    pack_dict[str(move_record.tracking_id.id)][2].append(move_record.id)       
                   
                    
                    
                #LOOP IN MOVES AND CREATE ALL DATA NECESSARY TO BUY LABELS FOR EASYPOST AND STAMPS

            for move_record in picking_out_record.move_lines:
            
                #Check if the label of pack of this move was already created with an previous move, if yes, jump the iteration.
                if pack_dict[str(move_record.tracking_id.id)][0]==True:
                    continue
                else:
                    pack_dict[str(move_record.tracking_id.id)][0]=True
                #############IF UPS, USPS or FEDEX###################
                
                # START CREATING ADDRESSES FOR EASYPOST
                if picking_out_record.shipping_carrier in ('UPS','USPS','FEDEX'):
                    verified_to_address = easypost.Address.create(
                                name = picking_out_record.partner_id.name,
                                street1 = picking_out_record.partner_id.street,
                                street2 = picking_out_record.partner_id.street2 or '',
                                city = picking_out_record.partner_id.city,
                                state = picking_out_record.partner_id.state_id.code,
                                zip = picking_out_record.partner_id.zip or '',
                                country = picking_out_record.partner_id.country_id.code or '',
                                phone = picking_out_record.partner_id.phone or move_record.location_id.partner_id.phone,
                                )
                    verified_from_address = easypost.Address.create(
                                name = move_record.location_id.partner_id.name,
                                street1 = move_record.location_id.partner_id.street,
                                street2 = move_record.location_id.partner_id.street2 or '',
                                city = move_record.location_id.partner_id.city,
                                state = move_record.location_id.partner_id.state_id.code,
                                zip = move_record.location_id.partner_id.zip,
                                phone = move_record.location_id.partner_id.phone,
                                country = move_record.location_id.partner_id.country_id.code or '',
                                )

                    #VERIFY FROM ADDRESSE
                    if picking_out_record.partner_id.country_id.code=="US":
                        try:
                            verified_from_address = verified_from_address.verify()
                        except easypost.Error as e:
                            stock_move_object.write(cr,uid,move_record.id,{'faulty': True, 'error_note': e})
                            stock_picking_out_object.write(cr,uid,picking_out_record.id,{'faulty': True, 'error_note': e})
                            #This variable is used to "continue" the parent loop (picking) into the next one, so that way, we will not process,
                            #a half of delivery and make exceptions in others, so its like this : if exception in a move, jump to next delivery, not only next move.
                            continue_picking_too = True 
                            break
                        #VERIFY TO ADDRESSE
                        try:
                            verified_to_address = verified_to_address.verify()
                        except easypost.Error as e:  
                            stock_move_object.write(cr,uid,move_record.id,{'faulty': True, 'error_note': e})
                            stock_picking_out_object.write(cr,uid,picking_out_record.id,{'faulty': True, 'error_note': e})
                            continue_picking_too = True 
                            break
                        
                    #CREATE PARCEL IF PREDEFINED PACKAGES
                    is_predefined_package = False
                    try:
                        if picking_out_record.shipping_carrier=="UPS" and picking_out_record.shipping_packaging_ups:
                            parcel = easypost.Parcel.create(
                                    predefined_package = picking_out_record.shipping_packaging_ups,
                                    weight = pack_dict[str(move_record.tracking_id.id)][1],
                                    )
                            is_predefined_package = True
                        elif picking_out_record.shipping_carrier=="USPS" and picking_out_record.shipping_packaging_usps:
                            parcel = easypost.Parcel.create(
                                    predefined_package = picking_out_record.shipping_packaging_usps,
                                    weight = pack_dict[str(move_record.tracking_id.id)][1],)
                            is_predefined_package = True
                        elif picking_out_record.shipping_carrier=="FEDEX"  and picking_out_record.shipping_packaging_fedex :
                            parcel = easypost.Parcel.create(
                                    predefined_package = picking_out_record.shipping_packaging_fedex,
                                    weight = pack_dict[str(move_record.tracking_id.id)][1],)
                            is_predefined_package = True
                    except easypost.Error as e:
                        # raise osv.except_osv(_('Error !'),  _(e))   
                        stock_move_object.write(cr,uid,move_record.id,{'faulty': True, 'error_note': e})
                        stock_picking_out_object.write(cr,uid,picking_out_record.id,{'faulty': True, 'error_note': e})
                        continue_picking_too = True 
                        break
                        
                    #CREATE PARCEL IF NOT PREDEFINED PACKAGES AND BOX IS SET AND DIMENSIONS AS WELL.
                    if is_predefined_package == False:
                        try:
                            if move_record.tracking_id.box_setup_id.length and move_record.tracking_id.box_setup_id.width\
                            and move_record.tracking_id.box_setup_id.height and move_record.override_weight!=0.0:
                                parcel = easypost.Parcel.create(
                                          length = move_record.tracking_id.box_setup_id.length,
                                          width  = move_record.tracking_id.box_setup_id.width,
                                          height = move_record.tracking_id.box_setup_id.height,
                                          weight = pack_dict[str(move_record.tracking_id.id)][1],
                                )
                        except easypost.Error as e:
                            # raise osv.except_osv(_('Error !'),  _(e))
                            stock_move_object.write(cr,uid,move_record.id,{'faulty': True, 'error_note': e})
                            stock_picking_out_object.write(cr,uid,picking_out_record.id,{'faulty': True, 'error_note': e})
                            continue_picking_too = True 
                            break

                    #Create Customs for EasyPost
                    customs_items = []
                    customs_info = False
                    if move_record.tracking_id.content_type:
                        for customs_move_id in pack_dict[str(move_record.tracking_id.id)][2]:
                            customs_move_record = stock_move_object.browse(cr, uid, customs_move_id)
                            customs_item = easypost.CustomsItem.create(
                                       description = customs_move_record.product_id.name,
                                       hs_tariff_number = customs_move_record.product_id.HSTariffNumber,
                                       origin_country = customs_move_record.location_id.partner_id.country_id.code,
                                       quantity = customs_move_record.product_qty,
                                       value = customs_move_record.product_id.list_price,
                                       weight = customs_move_record.override_weight)
                            customs_items.append(customs_item)

                        #Convert ContentType, From Stamps -> EasyPost
                        if move_record.tracking_id.content_type=="Other":
                            content_type = 'other'
                        elif move_record.tracking_id.content_type=="Document":  
                            content_type = "documents"
                        elif move_record.tracking_id.content_type=="Gift":
                            content_type = "gift"
                        elif move_record.tracking_id.content_type=="Commercial Sample":
                            content_type = "sample"
                        elif move_record.tracking_id.content_type=="Merchandise":
                            content_type = "merchandise"
                        elif move_record.tracking_id.content_type=="Returned Goods":
                            content_type = "returned_goods"
                        
                        if move_record.tracking_id.content_type=="Other":
                            customs_info = easypost.CustomsInfo.create(
                                                    contents_type = content_type, 
                                                    customs_items = customs_items,
                                                   contents_explanation = move_record.tracking_id.other_describe)
                        else:
                            customs_info = easypost.CustomsInfo.create(
                                                    contents_type = content_type, 
                                                    customs_items = customs_items)
                                               # customs_certify = move_record.tracking_id.customs_certify or '',
                                               # customs_signer = picking_out_record.partner_id.name or '', 

                                               # eel_pfc = move_record.tracking_id.eel_pfc,
                                               # non_delivery_option = "return",
                                               # restriction_type = move_record.tracking_id.restriction_type,
                                               # restriction_comments = move_record.tracking_id.restriction_comments,
                  
                    # CREATE SHIPEMENT, in the shipment dictionary, so that way we will not buy any shipement in a delivery order, untill we can create all shipments
                    #of the same delivery successfully.. 
                    if customs_info:
                        if picking_out_record.partner_id.address_validation.address_validation==False:
                            shipment[str(move_record.tracking_id.id)] = easypost.Shipment.create(
                                        to_address = verified_to_address,
                                        from_address = verified_from_address,
                                        parcel = parcel,
                                        customs_info = customs_info,
                                        options = {'address_level_validation':0})
                        else:
                            shipment[str(move_record.tracking_id.id)] = easypost.Shipment.create(
                                        to_address = verified_to_address,
                                        from_address = verified_from_address,
                                        parcel = parcel,
                                        customs_info = customs_info,)
                    else:
                        if picking_out_record.partner_id.address_validation==False:
                            shipment[str(move_record.tracking_id.id)] = easypost.Shipment.create(
                                        to_address = verified_to_address,
                                        from_address = verified_from_address,
                                        parcel = parcel,
                                        options = {'address_level_validation':0})
                        else:
                            shipment[str(move_record.tracking_id.id)] = easypost.Shipment.create(
                                        to_address = verified_to_address,
                                        from_address = verified_from_address,
                                        parcel = parcel)
                                        
                    # Check if there is no rate Proposed for label.
                    if len(shipment[str(move_record.tracking_id.id)].rates)==0:
                        stock_move_object.write(cr,uid,move_record.id,{'faulty': True, 'error_note': 'There is no rate proposed corresponding to your packages and parameters.'})
                        stock_picking_out_object.write(cr,uid,picking_out_record.id,{'faulty': True, 'error_note': 'There is no rate proposed corresponding to your packages and parameters.'})
                        continue_picking_too = True 
                        break
                    
                    #Check if there is no rate for the Shipping_Service Choosed in delivery Order.
                    shipment_buy_response = False
                    string_list_of_rates = "\n"
                    for shipment_rate in shipment[str(move_record.tracking_id.id)].rates:
                        string_list_of_rates += "\n - %s" % (shipment_rate['service'],)
                        if picking_out_record.shipping_carrier=="UPS":
                            if shipment_rate['service'].upper()==picking_out_record.shipping_service_ups and shipment_rate['carrier']=="UPS":
                                shipment_buy_response = True

                        if picking_out_record.shipping_carrier=="USPS":
                            if shipment_rate['service'].upper()==picking_out_record.shipping_service_usps and shipment_rate['carrier']=="USPS":
                                shipment_buy_response = True

                        if picking_out_record.shipping_carrier=="FEDEX":
                            if shipment_rate['service'].upper()==picking_out_record.shipping_service_fedex and shipment_rate['carrier']=="FEDEX":
                                shipment_buy_response = True
                                
                    #Raise the message if no corresponding rate is available for the choosen service in delivery.
                    if not shipment_buy_response:
                        string_error = 'The selected combination of Carrier, Service and/or Packaging is not available \
                                                for Delivery Order %s. \n TIP: This is the only available rates corresponding to \
                                                your Packing configurations : %s' % (picking_out_record.name ,string_list_of_rates,)
                        stock_move_object.write(cr,uid,move_record.id,{'faulty': True, 'error_note': string_error})
                        stock_picking_out_object.write(cr,uid,picking_out_record.id,{'faulty': True, 'error_note': string_error})
                        continue_picking_too = True 
                        break

                        
                ######START OF COMMENTING STAMPS 
                elif picking_out_record.shipping_carrier == 'USPS-STAMPS': # IF USPS-STAMPS
                    verified_from_address = self.get_stamps_address(stamps_service,\
                                                                move_record.location_id.partner_id.name,\
                                                                move_record.location_id.partner_id.street,\
                                                                move_record.location_id.partner_id.street2 or '',
                                                                move_record.location_id.partner_id.city,
                                                                move_record.location_id.partner_id.state_id.code,
                                                                move_record.location_id.partner_id.zip,
                                                                move_record.location_id.partner_id.country_id.code,
                                                                move_record.location_id.partner_id.phone)

                    verified_to_address = self.get_stamps_address(stamps_service,\
                                                                picking_out_record.partner_id.name,\
                                                                picking_out_record.partner_id.street,\
                                                                picking_out_record.partner_id.street2 or '',
                                                                picking_out_record.partner_id.city,
                                                                picking_out_record.partner_id.state_id.code,
                                                                picking_out_record.partner_id.zip,
                                                                picking_out_record.partner_id.country_id.code,
                                                                picking_out_record.partner_id.phone or move_record.location_id.partner_id.phone)
                                                                
                    #Create Rates for USPS-STAMPS
                    ret_val = stamps_service.create_shipping()
                    formated_date = datetime.strptime(picking_out_record.date, '%Y-%m-%d %H:%M:%S').date()
                    ret_val.ShipDate = formated_date.isoformat()
                    ret_val.FromZIPCode = move_record.location_id.partner_id.zip
                    ret_val.ToZIPCode = picking_out_record.partner_id.zip
                    if picking_out_record.shipping_packaging_usps_stamps:
                        ret_val.PackageType = caps_conversion_dict[picking_out_record.shipping_packaging_usps_stamps] # This dict is used to swith to unCapsed strings
                        ret_val.WeightOz = pack_dict[str(move_record.tracking_id.id)][1]
                    else:
                        ret_val.PackageType = 'Package'
                        ret_val.WeightOz = pack_dict[str(move_record.tracking_id.id)][1]
                        ret_val.Length = move_record.tracking_id.box_setup_id.length
                        ret_val.Width = move_record.tracking_id.box_setup_id.width
                        ret_val.Height = move_record.tracking_id.box_setup_id.height
                        
                    #Try to get label rates, if error, note the error in fields. (Generally the Old Date Error can be raised here ) 
                    try:
                        rates = stamps_service.get_rates(ret_val)
                    except WebFault as e:
                        stock_move_object.write(cr,uid,move_record.id,{'faulty': True, 'error_note': e})
                        stock_picking_out_object.write(cr,uid,picking_out_record.id,{'faulty': True, 'error_note': e})
                        continue_picking_too = True
                        break
                    #Create Customs for STAMPS
                    customs_lines = []
                    customs = False
                    if move_record.tracking_id.content_type:
                        for customs_move_id in pack_dict[str(move_record.tracking_id.id)][2]:
                            customs_line = self.create_customsline(stamps_service, Description = move_record.product_id.name, Quantity = move_record.product_qty, Value = move_record.product_id.list_price, WeightLb = move_record.override_weight, HSTariffNumber = move_record.product_id.HSTariffNumber, CountryOfOrigin = move_record.location_id.partner_id.country_id.code)
                            customs_lines.append(customs_line)
                        # assert False, customs_lines  
                        customs = self.create_customs(stamps_service, move_record.tracking_id.content_type, move_record.tracking_id.other_describe,customs_lines) # Both parameters are required
                        # assert False, customs
                    #Buy with the rate corresponding to the service in delivery order, If no Rate corresponding to it, raise an error, with possible services to choose.
                    shipment_buy_response = False
                    string_list_of_rates = "\n"
                    for rate in rates:
                        string_list_of_rates += "\n - %s" % (rate['ServiceType'],)
                        if rate['ServiceType']==picking_out_record.shipping_service_usps_stamps :
                            shipment_buy_response = True #SET THIS TO TRUE, USE IT IN BOTTOM TO KNOW IF LABEL WAS CREATED, AND SERVICE RATE WAS FOUND.
                            ret_val.Amount = rate.Amount
                            ret_val.ServiceType = picking_out_record.shipping_service_usps_stamps
                            ret_val.DeliverDays = rate.DeliverDays
                            ret_val.DimWeighting = rate.DimWeighting
                            ret_val.Zone = rate.Zone
                            ret_val.RateCategory = rate.RateCategory
                            ret_val.ToState = rate.ToState
                            # add_on = stamps_service.create_add_on()
                            # add_on.AddOnType = "US-A-DC"
                            # ret_val.AddOns.AddOnV6.append(add_on)
                            transaction_id = datetime.now().isoformat()
                            stamps_tuple[str(move_record.tracking_id.id)] = (verified_from_address, verified_to_address, ret_val,transaction_id, customs)
                            stamps_total_amount_purchase += float(rate.Amount)
                            
                    #Raise the message if no corresponding rate is available for the choosen service in delivery.
                    if not shipment_buy_response: 
                        string_error = 'The selected combination of Carrier, Service and/or Packaging is not available \
                                                                                    for Delivery Order %s. \n TIP: This is the only available rates corresponding to \
                                                                                    your Packing configurations : %s' % (picking_out_record.name ,string_list_of_rates,)
                        stock_move_object.write(cr,uid,move_record.id,{'faulty': True, 'error_note': string_error})
                        stock_picking_out_object.write(cr,uid,picking_out_record.id,{'faulty': True, 'error_note': string_error})
                        continue_picking_too = True 
                        break
                        
                   ######END OF COMMENTING STAMPS 
                elif picking_out_record.shipping_carrier in ('UPS WorldShip CSV','USPS DataPac CSV'): # IF UPS WorldShip CSV
                    pass
                    
                    
            #Check if there was any "continue" in the previous move line, if yes, jump to the next
            if continue_picking_too:
                continue
                
            else:
                #Delete any Faulty Error, or Notes.
                picking_out_record.write({'faulty':False, 'error_note':False})
                move_record.write({'faulty':False, 'error_note':False})
                #Buy with the rate corresponding to the service in delivery order, for all moves (packs) of the delivery order.
                # assert False, pack_dict.keys() + shipment.keys()
                do_not_create_labels = False
                
                #Purchase Balance.
                if picking_out_record.shipping_carrier == 'USPS-STAMPS':
                    transaction_id = datetime.now().isoformat()
                    account_informations = stamps_service.get_account()
                    current_balance = account_informations['AccountInfo']['PostageBalance']['AvailablePostage']
                    difference = math.ceil(stamps_total_amount_purchase - float(current_balance))
                    if difference < 0:
                        res_company_obj.write(cr, uid, picking_out_record.company_id.id, {'stamps_balance': - difference}, context)
                    else:
                        if difference<10:
                            res_company_obj.write(cr, uid, picking_out_record.company_id.id, {'stamps_balance': 10 - difference }, context)
                            difference = 10.0
                        else:
                            res_company_obj.write(cr, uid, picking_out_record.company_id.id, {'stamps_balance': 0.0}, context)
                            
                        result = stamps_service.add_postage(difference, transaction_id=transaction_id)
                        transaction_id = result.TransactionID
                        status = stamps_service.create_purchase_status()
                        seconds = 1
                        while result.PurchaseStatus in (status.Pending, status.Processing):
                            if seconds + 1 >= 60:
                                raise osv.except_osv(_('Stamps Postage Purchase Timeout!'),  _('Please check the next reasons, or contact Stamps Support for more informations:\n \
                                    - It may be due to connectivity issue with the Stamps server.\n \
                                    - It maybe due to some problemes with your method of payments you configured in stamps.\n \
                                    This is the TransactionID you can contact Stamps support with : %s ' % (str(result.TransactionID),)))
                            else:
                                seconds += 1
                            print "Waiting {0:d} seconds to get status...".format(seconds)
                            sleep(seconds)
                            result = stamps_service.get_postage_status(transaction_id)
                email_sting_tracking_numbers = ''                  
                for pack_id in pack_dict.keys():
                    if picking_out_record.shipping_carrier in ('UPS','USPS','FEDEX'): #IF UPS, USPS or FEDEX
                            
                        for shipment_rate in shipment[pack_id].rates:
                            string_list_of_rates += "\n - %s" % (shipment_rate['service'],)
                            if picking_out_record.shipping_carrier=="UPS":
                                if shipment_rate['service'].upper()==picking_out_record.shipping_service_ups and shipment_rate['carrier']=="UPS":
                                    try:
                                        shipment_buy_response = shipment[pack_id].buy(rate = shipment_rate)
                                    except easypost.Error as e:
                                        stock_move_object.write(cr,uid,move_record.id,{'faulty': True, 'error_note': e})
                                        stock_picking_out_object.write(cr,uid,picking_out_record.id,{'faulty': True, 'error_note': e})
                                        do_not_create_labels = True
                                        break
                            
                            if picking_out_record.shipping_carrier=="USPS":
                                if shipment_rate['service'].upper()==picking_out_record.shipping_service_usps and shipment_rate['carrier']=="USPS":
                                    try:
                                        shipment_buy_response = shipment[pack_id].buy(rate = shipment_rate)
                                    except easypost.Error as e:
                                        stock_move_object.write(cr,uid,move_record.id,{'faulty': True, 'error_note': e})
                                        stock_picking_out_object.write(cr,uid,picking_out_record.id,{'faulty': True, 'error_note': e})
                                        do_not_create_labels = True
                                        break

                            if picking_out_record.shipping_carrier=="FEDEX":
                                if shipment_rate['service'].upper()==picking_out_record.shipping_service_fedex and shipment_rate['carrier']=="FEDEX":
                                    try:
                                        shipment_buy_response = shipment[pack_id].buy(rate = shipment_rate)
                                    except easypost.Error as e:
                                        stock_move_object.write(cr,uid,move_record.id,{'faulty': True, 'error_note': e})
                                        stock_picking_out_object.write(cr,uid,picking_out_record.id,{'faulty': True, 'error_note': e})
                                        do_not_create_labels = True
                                        break
                                        
                        if do_not_create_labels:
                            break
                        #CREATE INSURANCE IF IT IS CHECKED
                        total_insurance_amount = 0
                        for insurance_move_id in pack_dict[pack_id][2]:
                            insurance_move_record = stock_move_object.browse(cr, uid, insurance_move_id)
                            if insurance_move_record.insured:
                                total_insurance_amount += insurance_move_record.product_id.list_price
                        
                        if total_insurance_amount>0:
                            try:
                                shipment[pack_id].insure(amount = total_insurance_amount) 
                            except easypost.Error as e:
                                stock_move_object.write(cr,uid,move_record.id,{'faulty': True, 'error_note': e})
                                stock_picking_out_object.write(cr,uid,picking_out_record.id,{'faulty': True, 'error_note': e})
                                do_not_create_labels = True
                                break

                        if email_sting_tracking_numbers=='':
                            email_sting_tracking_numbers = str(shipment_buy_response['tracking_code'])
                        else:
                            email_sting_tracking_numbers = email_sting_tracking_numbers + ',' +str(shipment_buy_response['tracking_code'])


                        url_tracking_ref = False
                        if picking_out_record.shipping_carrier == "UPS":
                            url_tracking_ref = 'http://wwwapps.ups.com/WebTracking/track?track=yes&trackNums=' + shipment_buy_response['tracking_code'] +'&loc=en_us'
                        elif picking_out_record.shipping_carrier == "USPS":
                            url_tracking_ref = 'https://tools.usps.com/go/TrackConfirmAction?qtc_tLabels1=' + shipment_buy_response['tracking_code']
                        stock_move_object.write(cr,uid,pack_dict[pack_id][2],{'url_tracking_ref': url_tracking_ref,'carrier_tracking_ref': shipment_buy_response['tracking_code']})
                        stock_picking_out_object.write(cr,uid,picking_out_record.id,{'url_tracking_ref': url_tracking_ref,'carrier_tracking_ref': shipment_buy_response['tracking_code']})

                        #Prepare data for image and attachement creation.
                        image_url=False
                        image_url=shipment_buy_response.postage_label.label_url
                        resource = urllib.urlopen(image_url) 
                        data = base64.encodestring(resource.read())
                                  
                    elif picking_out_record.shipping_carrier == 'USPS-STAMPS': #IF STAMPS
                        try:
                            if stamps_tuple[pack_id][4]:
                                label = stamps_service.get_label_customised(stamps_tuple[pack_id][0], 
                                                                                   stamps_tuple[pack_id][1], 
                                                                                   stamps_tuple[pack_id][2],
                                                                                   stamps_tuple[pack_id][3],
                                                                                   stamps_tuple[pack_id][4])
                            else:
                                label = stamps_service.get_label(stamps_tuple[pack_id][0], 
                                                                                   stamps_tuple[pack_id][1], 
                                                                                   stamps_tuple[pack_id][2],
                                                                                   stamps_tuple[pack_id][3])
                        except WebFault as e:
                            stock_move_object.write(cr,uid,move_record.id,{'faulty': True, 'error_note': e})
                            stock_picking_out_object.write(cr,uid,picking_out_record.id,{'faulty': True, 'error_note': e})
                            do_not_create_labels = True
                            break
                        # assert False, type(label.TrackingNumber)
                        stamps_label_tracking_id = label.TrackingNumber
                        stamps_label_URL = label.URL
                        url_tracking_ref = False
                        url_tracking_ref = 'https://tools.usps.com/go/TrackConfirmAction?qtc_tLabels1=' + stamps_label_tracking_id
                        stock_move_object.write(cr,uid,pack_dict[pack_id][2],{'url_tracking_ref': url_tracking_ref,'carrier_tracking_ref': stamps_label_tracking_id})
                        stock_picking_out_object.write(cr,uid,picking_out_record.id,{'url_tracking_ref': url_tracking_ref,'carrier_tracking_ref': stamps_label_tracking_id})
                        #Prepare data for image and attachement creation.
                        image_url=stamps_label_URL
                        resource = urllib.urlopen(image_url) 
                        data = base64.encodestring(resource.read())
                        if email_sting_tracking_numbers=='':
                            email_sting_tracking_numbers = str(stamps_label_tracking_id)
                        else:
                            email_sting_tracking_numbers = email_sting_tracking_numbers + ',' +str(stamps_label_tracking_id)
                    elif picking_out_record.shipping_carrier == 'UPS WorldShip CSV':
                        image_url = False
                        data = False
                    elif picking_out_record.shipping_carrier == 'USPS DataPac CSV':
                        image_url = False
                        data = False
                    #CREATING AND AFFECTING LABELS FOR EVERY MOVE LINE.  
                    for pack_move_id in pack_dict[pack_id][2]:
                        if picking_out_record.shipping_carrier not in ('UPS WorldShip CSV','USPS DataPac CSV'):
                            attached_already = attach_object.search(cr, uid, [('res_id','=',pack_move_id)])
                            attach_object.unlink(cr, uid, attached_already, context=None)
                            a_id = attach_object.create(cr, uid, {'name': image_url, 'res_model': 'stock.move','res_id': pack_move_id,'type': 'binary', 'db_datas':data })
                
                #If there was an error while creating LABELS for Packs, don't create Picking_Labels, and do not close moves, and deliveries
                if do_not_create_labels:
                    continue
                else: 
                    #CREATE PICKING_OUT_LABEL CORESPONDING TO ALL CREATED LABELS IN THIS DELIVERY, 
                    #AND IT'S BY WAREHOUSE: CREATE ONE RECORD FOR EVERY WAREHOUSE, AND EVERY RECORD WILL CONTAIN CORRESPONDING DELIVERY ORDERS
                    stock_picking_out_object.write(cr,uid,picking_out_record.id,{'state':'done','last_status_emailed': 'done','list_tracking_numbers': email_sting_tracking_numbers,})
                    if email_sting_tracking_numbers!='':
                        stock_picking_out_object.send_email_tracking(cr, uid,picking_out_record.id)
                    if str(move_record.location_id.id) in list(warehouse_ids_dict.keys()):
                        stock_picking_out_label_id = stock_picking_out_label_object.write(cr,uid,warehouse_ids_dict[str(move_record.location_id.id)],\
                                                                                    {'picking_ids':[(4,picking_out_record.id)]})
                    else:
                        stock_picking_out_label_id = stock_picking_out_label_object.create(cr,uid,{'date':time.strftime('%Y-%m-%d %H:%M:%S'),\
                                                                                   'location_id':move_record.location_id.id,\
                                                                                    'picking_ids':[(4,picking_out_record.id)]})
                        string_warehouse_id = str(move_record.location_id.id)
                        warehouse_ids_dict[string_warehouse_id]=stock_picking_out_label_id
 
        return True



    # def create_shipment_simplest_2(self, cr, uid, ids, context=None):
        # postmaster.config.api_key = 'tt_NjIxMDAxOndJc2kweENoM0hOVnpfU0g1dl90X280TUJPYw'
        # sale_obj= self.pool.get('sale.order')
        # attach_object=self.pool.get('ir.attachment')
        # product_object=self.pool.get('product.product')
        # carrier_obj=self.pool.get('delivery.carrier')
        # print "============>",context
        # for data in self.browse(cr,uid,ids):
            # packages=[]
            # print "===========>",data.shipping_carrier
            # if not data.shipping_carrier:
                # if context.get('From_wizard_lable'):
                    # data.write({'faulty':True, 'error_note':'Please Enter the Shipping Carrier for order'})
                # else:
                    # raise osv.except_osv(_('Error!'),  _('Please Enter the Shipping Carrier for order  %s' % (data.name)))
    # #            raise osv.except_osv(_('Error!'),  _('Please Enter the Shipping Carrier'))
            # if data.shipping_carrier == 'fedex':
                # carrier='fedex'
                # if not data.shipping_service_fedex:
                    # if context.get('From_wizard_lable'):
                        # data.write({'faulty':True, 'error_note':'Please Enter the Shipping service'})
                    # else:
                        # raise osv.except_osv(_('Error!'),  _('Please Enter the Shipping service  for %s' % (data.name)))
                # if not data.shipping_packaging_fedex:
                    # if context.get('From_wizard_lable'):
                        # data.write({'faulty':True, 'error_note': 'Please Select the Packaging'})
                    # else:
                        # raise osv.except_osv(_('Error!'),  _('Please Select the Packaging for %s' % (data.name)))
                # packaging = data.shipping_packaging_fedex
                # service=data.shipping_service_fedex
            # elif data.shipping_carrier=='usps':
                # carrier='usps'
                # if not data.shipping_service_usps:
                    # if context.get('From_wizard_lable'):
                        # data.write({'faulty':True, 'error_note': 'Please Enter the Shipping service'})
                    # else:
                        # raise osv.except_osv(_('Error!'),  _('Please Enter the Shipping service for  %s' % (data.name)))
                # if not data.shipping_packaging_usps:
                    # if context.get('From_wizard_lable'):
                        # data.write({'faulty':True, 'error_note':'Please Select the Packaging'})
                    # else:
                        # raise osv.except_osv(_('Error!'),  _('Please Select the Packaging for %s' % (data.name)))
                # packaging = data.shipping_packaging_usps
                # service=data.shipping_service_usps
            
            # else:
                # carrier='ups'
                # if not data.shipping_service_ups:
                    # if context.get('From_wizard_lable'):
                        # data.write({'faulty':True, 'error_note': 'Please Enter the Shipping service'})
                    # else:
                        # raise osv.except_osv(_('Error!'),  _('Please Enter the Shipping service for %s' % (data.name)))
                # if not data.shipping_packaging_ups:
                    # if context.get('From_wizard_lable'):
                        # data.write({'faulty':True, 'error_note': 'Please Select the Packaging'})
                    # else:
                        # raise osv.except_osv(_('Error!'),  _('Please Select the Packaging for %s' % (data.name)))
                # packaging = data.shipping_packaging_ups
                # service= data.shipping_service_ups

            # track_no = ''
            # for move in data.move_lines:
                # if move.tracking_id:
                    # if not move.tracking_id.name==track_no:
                        
                        # if not move.product_id.packaging:
                            # if move.product_id.weight_net and move.product_id.length and move.product_id.width and move.product_id.height:
                                 # p={
                                    # 'type' :packaging,
                                    # 'weight': move.product_id.weight_net,
                                    # 'length': move.product_id.length,
                                    # 'width': move.product_id.width,
                                    # 'height': move.product_id.height,
                             # #       'customs': str(move.id),
                                  # }
                                 # packages.append(p)
                                 # p={}
                            # else:
                                # if context.get('From_wizard_lable'):
                                    # data.write({'faulty':True, 'error_note': 'Please Enter the product height,width,length,weight'})
                                # else:
                                    # raise osv.except_osv(_('Error!'),  _('Please Enter the product height,width,length,weight'))
                        # else:
                            # height = False
                            # if move.product_id.packaging[0].height:
                                # height=move.product_id.packaging[0].height
                            # else:
                                # if context.get('From_wizard_lable'):
                                    # data.write({'faulty':True, 'error_note': 'Please Enter the package height in product'})
                                # else:
                                    # raise osv.except_osv(_('Error!'),  _('Please Enter the package height in product %s' % (data.name)))
                            # length = False
                            # if move.product_id.packaging[0].length:
                                # length=move.product_id.packaging[0].length
                            # else:
                                # if context.get('From_wizard_lable'):
                                    # data.write({'faulty':True, 'error_note': 'Please Enter the package length in product'})
                                # else:
                                    # raise osv.except_osv(_('Error!'),  _('Please Enter the package length in product %s' % (data.name)))
                            # width = False
                            # if move.product_id.packaging[0].width:
                                # width=move.product_id.packaging[0].width
                            # else:
                                # if context.get('From_wizard_lable'):
                                    # data.write({'faulty':True, 'error_note': 'Please Enter the package width in product'})
                                # else:
                                    # raise osv.except_osv(_('Error!'),  _('Please Enter the package width in product %s' % (data.name)))
                            # print'move.product_id.packaging[0].weight',move.product_id.packaging[0].weight
                            # weight = False
                            # if move.product_id.packaging[0].weight:
                                # weight=move.product_id.packaging[0].weight
                            # else:
                                # if context.get('From_wizard_lable'):
                                    # data.write({'faulty':True, 'error_note': 'Please Enter the package weight in product'})
                                # else:
                                    # raise osv.except_osv(_('Error!'),  _('Please Enter the package weight in product %s' % (data.name)))
                            # p={
                                # 'type' :packaging,
                                # 'weight': weight,
                                # 'length': length,
                                # 'width': width,
                                # 'height': height,
                          # #      'customs': str(move.id),
                                # }
                            # packages.append(p)
                            # p={}
                        # print packages
                    # track_no=move.tracking_id.name

            # if data.origin:
                             
                # origin = data.origin
               # # origin = data.origin[:5]
                # sale_id=sale_obj.search(cr,uid,[('name','=',origin)])
                # sale_data=sale_obj.browse(cr,uid,sale_id)
              
                # if not sale_data[0].shop_id:
                    # if context.get('From_wizard_lable'):
                        # data.write({'faulty':True, 'error_note': 'Please Enter the Shop'})
                    # else:
                        # raise osv.except_osv(_('Error!'),  _('Please Enter the Shop %s' % (data.name)))

                # if not sale_data[0].shop_id.company_id.phone:
                    # if context.get('From_wizard_lable'):
                        # data.write({'faulty':True, 'error_note': 'Please Enter Phone number For Sender'})
                    # else:
                        # raise osv.except_osv(_('Error!'),  _('Please Enter Phone number For Sender  %s' % (data.name)))
                # if not data.partner_id.phone:
                    # if context.get('From_wizard_lable'):
                        # data.write({'faulty':True, 'error_note': 'Please Enter Phone number For Receipent'})
                    # else:
                        # raise osv.except_osv(_('Error!'),  _('Please Enter Phone number For Receipent %s' % (data.name)))

                # if not data.partner_id.street:
                    # if context.get('From_wizard_lable'):
                        # data.write({'faulty':True, 'error_note': 'Please Enter street For Receipent'})
                    # else:
                        # raise osv.except_osv(_('Error!'),  _('Please Enter street For Receipent %s' % (data.name)))
                # if not sale_data[0].shop_id.company_id.street:
                    # if context.get('From_wizard_lable'):
                        # data.write({'faulty':True, 'error_note': 'Please Enter street For Sender'})
                    # else:
                        # raise osv.except_osv(_('Error!'),  _('Please Enter street For Sender  %s' % (data.name)))


                # if not sale_data[0].shop_id.company_id.city:
                    # if context.get('From_wizard_lable'):
                        # data.write({'faulty':True, 'error_note': 'Please Enter City For Sender'})
                    # else:
                        # raise osv.except_osv(_('Error!'),  _('Please Enter City For Sender  %s' % (data.name)))
                # if not data.partner_id.city:
                    # if context.get('From_wizard_lable'):
                        # data.write({'faulty':True, 'error_note': 'Please Enter city For Receipent'})
                    # else:
                        # raise osv.except_osv(_('Error!'),  _('Please Enter city For Receipent  %s' % (data.name)))

                # if not sale_data[0].shop_id.company_id.state_id:
                    # if context.get('From_wizard_lable'):
                        # data.write({'faulty':True, 'error_note': 'Please Enter state For Sender'})
                    # else:
                        # raise osv.except_osv(_('Error!'),  _('Please Enter state For Sender  %s' % (data.name)))

                # if not data.partner_id.state_id:
                    # if context.get('From_wizard_lable'):
                        # data.write({'faulty':True, 'error_note': 'Please Enter State For Receipent'})
                    # else:
                        # raise osv.except_osv(_('Error!'),  _('Please Enter State For Receipent  %s' % (data.name)))

                # if not sale_data[0].shop_id.company_id.zip:
                    # if context.get('From_wizard_lable'):
                        # data.write({'faulty':True, 'error_note': 'Please Enter zip For Sender'})
                    # else:
                        # raise osv.except_osv(_('Error!'),  _('Please Enter zip For Sender  %s' % (data.name)))
                # if not data.partner_id.zip:
                    # if context.get('From_wizard_lable'):
                        # data.write({'faulty':True, 'error_note': 'Please Enter zip For Receipent'})
                    # else:
                        # raise osv.except_osv(_('Error!'),  _('Please Enter zip For Receipent  %s' % (data.name)))

    # #           try:
    # #           print"====>>",sale_data[0].shop_id.company_id.phone
                # to={
                    # 'company':data.partner_id.name,
                    # 'contact': data.partner_id.name,
                    # 'line1': data.partner_id.street,
                    # 'line2': data.partner_id.street2 or '',
                    # 'city': data.partner_id.city,
                    # 'state': data.partner_id.state_id.code,
                    # 'zip_code': data.partner_id.zip,
                    # 'phone_no': data.partner_id.phone,
                # }
                # packages=packages
                # carrier=str(carrier)
                # service=service
                # from_={
                    # 'contact': sale_data[0].shop_id.company_id.name,
                    # 'line1': sale_data[0].shop_id.company_id.street,
                    # 'city': sale_data[0].shop_id.company_id.city,
                    # 'state': sale_data[0].shop_id.company_id.state_id.code,
                    # 'zip_code': sale_data[0].shop_id.company_id.zip,
                    # 'phone_no': sale_data[0].shop_id.company_id.phone,
                    # }

                # try:
                    # shipment = postmaster.Shipment.create(to=to,packages=packages,carrier=carrier,service=service,from_=from_,)
    ##                   pprint(shipment._data)
                    # print  "=====shipment====>",shipment._data
                    # count=0
                    # name_label=[]
                    # image_url=False
                    # print'*********shipment.packages',shipment.packages
                    # for label in shipment.packages:
                        # image_url="https://www.postmaster.io"+label.get('label_url')
                        # print "====>",image_url
                        # if image_url:
                            # a_id = attach_object.create(cr, uid, {'name': 'Label', 'res_model': 'stock.picking.out', 'res_id': ids[0],'type': 'url', 'url': image_url})
                            # data.write({'faulty':False, 'error_note':False})
                    
                    # ################ Below code add by 4devnet to assign tracking(pack) ids to attachments(labels)############## 
                    # cr.execute('select distinct tracking_id from stock_move where picking_id=%s',(data.id,))
                    # result= cr.fetchall()
                    # i = 0
                    # for rs in result:
                        # cr.execute("select min(id) from ir_attachment where res_id=%s",(data.id,))
                        # ir_res = cr.fetchall()
                        # ir_id = ir_res[0][0] + i
                        # print str(ir_id)
                        # print 'ir_id'
                        # cr.execute('update ir_attachment set tracking_id=%s where id=%s',(rs[0],ir_id,))
                        # i += 1                  
                    # ############################################################################################################
                    
                    # carr_ids = carrier_obj.search(cr, uid, [('name','=',shipment.carrier)])
                    # carrier_code = shipment.carrier

                    # if carr_ids:
                        # c_id = carr_ids[0]
                        # #carrier_obj.write(cr, uid, c_id,{'metapack_carrier_code': carrier_code})
                    # else:
                        # prod_ids = product_object.search(cr, uid, [('type', '=','service')])
                        # if prod_ids:
                            # p_id  = prod_ids[0]
                        # else:
                            # p_id  = product_object.create(cr, uid, {'name': 'Shipping and Handling', 'type':"service" ,'categ_id': 1})
            # #            patner_id = partner_object.create(cr, uid, {'name': shipment.carrier})
                        # c_id = carrier_obj.create(cr, uid, {'name': shipment.carrier , 'metapack_carrier_code':carrier_code, 'product_id': p_id,'partner_id': sale_data[0].shop_id.company_id.id})
                    # self.write(cr,uid,ids,{'carrier_tracking_ref':shipment.tracking[0],'carrier_id':c_id})
                    # self.do_partial(cr, uid, ids, context=None)
                # except Exception, e:
                    # if context.get('From_wizard_lable'):
                        # data.write({'faulty':True, 'error_note':str(e)})
                    # else:
                        # self.write(cr,uid,ids,{'faulty':True,'error_note':e})
                        # raise osv.except_osv(('Error !'), ('%s') % (e))

                

                # '''for label in shipment.packages:
                    # print"===", label.get('label_url')
                    # count+=1
                    # image_url="https://www.postmaster.io"+label.get('label_url')
                    # file_n = "/var/www/postmaster/label_"+str(count)+('_')+str(shipment.tracking[0])+".png"

                    # file = urllib.urlopen(image_url)
                    # im = cStringIO.StringIO(file.read())
                    # img = Image.open(im)
    # #                urllib.urlretrieve(image_url, file_n)
    # #                img = Image.open(file_n)
    # #                figure = plt.gcf() # get current figure
    # #                img.drawHeight = 2*inch
    # #                img.drawWidth = 1*inch
    # #                img._restrictSize(6 * inch, 4 * inch)
    # #                im2 = img.resize((128,128),Image.ANTIALIAS)
                    # img.save("/var/www/postmaster/label_"+str(count)+('_')+str(shipment.tracking[0])+".pdf")
                    # name_label.append(str(count)+('_')+str(shipment.tracking[0])+".pdf")
        # #            file1 = "label" +str(picking.id)+".pdf"
                # print name_label
                # file1 = "/var/www/postmaster/master_"+str(shipment.tracking[0])+".pdf"
                # if os.path.exists(file1):
                    # os.remove(file1)
                # merger = PdfFileMerger()
                # #for manifest

                # for label in  name_label:
                    # print"labelssss------"
                    # child_pdf_file= "/var/www/postmaster/label_"+label
                    # print child_pdf_file
                    # merger.append(PdfFileReader(file(child_pdf_file, 'rb')))
                # merger.write(file1)'''
    # #            f=open("/var/www/master.pdf", 'rb')
            # self.write(cr, uid, ids[0], {'state': 'done'})
                
        # return True
        
        
        
        
        
            #     def print_multiple_label(self,cr,uid,ids,context=None):
    #         
    #         if ids:
    #             abc = str(tuple(int(i) for i in ids))
    #                 
    #             k = 0
    #             for i in ids:
    #                 k+=1
    #               
    #             if  k==1:
    #                 newstr = abc.replace(",", "")
    #              
    #             else:
    #                 newstr = str(tuple(int(i) for i in ids))
    #                  
    #            #     newstr = abc.replace(",", "")
    #                
    #              #   query = "select distinct picking_id from shipment_summary where id IN %s" % str(tuple(int(i) for i in ids))
    #             query = "select count(distinct(location_id)) from stock_move where picking_id IN %s" %(newstr)
    #             print 'query print'
    #             print query
    #             cr.execute(query)
    #             move_id = cr.fetchall()
    #             print 'hasu move id print'
    #             print move_id
    #             
    #     #         
    #     #         print 'hasu ids print'
    #     #         print ids
    #     #         cr.execute(""" select count(distinct(location_id)) from stock_move where picking_id in (1280,1281) """)
    #     #         move_id = cr.fetchall()
    #             
    #             if move_id[0][0] > 1:
    #                 
    #                     raise osv.except_osv(_('Message!'),  _("Warehouses are different so label can not be print !"))
    #         return { 'type': 'ir.actions.report.xml',
    #                  'report_name':'webkit.html',
    #                  'report_file' : 'shipping_postmaster/report/report_webkit.mako',
    #                  'datas': 
    #                         { 'model':'stock.picking.out',
    #                           'id': 1280,
    #                           'report_type': 'webkit', 
    #                           'file' : 'shipping_postmaster/report/report_webkit.mako',
    #                           'name' : 'webkit.html',
    #                         }, 
    #                 'nodestroy': True }
        #    return {
    #                 'type' : 'ir.actions.report.xml',
    #                 'report_name' : 'webkit.html',
    #                 'model' : 'stock.picking.out',
    #                 'report_type' : 'webkit',
    #                 'file' : 'shipping_postmaster/report/report_webkit.mako',
    #                 'name' : 'webkit.html',
    #                 'id' :  "report_webkit" 
    #                 }
    #         return {
    #                 'type' : 'ir.actions.report.xml',
    #                 'report_name' : 'webkit.html',
    #                 'datas' :{
    #                           'model' : 'stock.picking.out',
    #                           'id' : context.get('active_ids') or False,
    #                           'ids' : context.get('active_ids') and context.get('active_ids') or [],
    #                           'report_type' : 'report_webkit',
    #                           },
    #                 'nodestroy' : False
    #                 }

    #         if context is None:
    #             context = {}
    #         datas = {'ids': context.get('active_ids', [])}
    #         datas['model'] = 'stock.picking.out'
    #         datas['report_file'] = 'shipping_postmaster/report/report_webkit.mako'
    #         return {
    #         'type': 'ir.actions.report.xml',
    #         'report_name': 'webkit.html',
    #         'datas': datas,
    #         }
    #         return {
    #             report_sxw.report_sxw('report.webkit.html',
    #                        'stock.picking.out',
    #                        'shipping_postmaster/report/report_webkit.mako',
    #                        parser=report_webkit_html,
    #                        
    #                        )
    #             }
            #return True
            
    # def merge_orders(self, cr, uid, ids, context=None):
        
        # if ids:
            # abc = str(tuple(int(i) for i in ids))
            
            # k = 0
            # for i in ids:
                # k+=1
              
            # if k==1:
                # newstr = abc.replace(",", "")
            # else:
                # newstr = str(tuple(int(i) for i in ids)) 
            
            # query = "select distinct warehouse_id from stock_picking where id IN %s" %(newstr)
            # cr.execute(query)
            # result_warehouse_id = cr.fetchall()
            
            # query1 = "select distinct partner_id from stock_picking where id IN %s" %(newstr)
            # cr.execute(query1)
            # result_partner_id = cr.fetchall()
            
            # move_update = "update stock_move set picking_id=%s where picking_id IN %s" %(ids[0],newstr)
            # cr.execute(move_update)
           
            
            # ware_length = len(result_warehouse_id)
            # partner_length = len(result_partner_id)
            
            # if (ware_length == 2):
                # raise osv.except_osv(_('Message!'),  _("Warehouses are different so records can not be merged !"))
            
            # if (partner_length == 2):
                # raise osv.except_osv(_('Message!'),  _("Customer are different so records can not be merged !"))
            
            # so = ''
            
            
            # for id in ids:
                
                # pick_obj  = self.browse(cr,uid,id)
                # source_document = pick_obj.origin
                
                # so = str(source_document) + ' ' +  str(so)
             
                # if ((pick_obj.state == 'done') or (pick_obj.state == 'done') or (pick_obj.state == 'label-sent')):
                    # raise osv.except_osv(_('Message!'),  _("Records can not be merged"))
            
            # cr.execute(""" update stock_picking set origin=%s,state='merge' where id=%s""",(so,ids[0],))
            
            
            # abc = str(tuple(int(i) for i in ids))
            
            # k = 0
            # for i in ids:
                # k+=1
              
            # if k==1:
                # newstr = abc.replace(",", "")
            # else:
                # newstr = str(tuple(int(i) for i in ids)) 
            
            # move_delete = "delete from stock_picking where id in %s and state<>'merge'" %(newstr)
            # cr.execute(move_delete)
            
        # return True



    #    def packaging_product(self, cr, uid, ids, context=None):
    #        stock_obj=self.pool.get('stock.tracking')
    #        move_obj = self.pool.get('stock.move')
    #        (data,)=self.browse(cr,uid,ids)
    #        count=0
    #        update_same=True
    #        today=datetime.date.today()
    #        for i in data.move_lines:
    #            print "******",i
    #            if not i.product_id.packaging:
    #                continue
    #            
    #            package=i.product_id.packaging[0].qty
    #            if package > i.product_qty:
    #                continue
    #            else:
    #                print i.product_qty,package
    #                t=i.product_qty/package
    #                print "====",t,int(round(t))
    #                s=i.product_qty%package
    #                print'sssssssssssssssss', s
    #                print'tttttttttttt', t
    #
    #                if s > 0.0 :
    #                    p=int(math.ceil(t))
    ##                    p=abs(t)+1
    #                else:
    #                    p=int(math.ceil(t))
    #                print"--ppp--", p
    #
    #                order_qty=i.product_qty
    #                pack_qty=i.product_id.packaging[0].qty
    #                print "===",count
    #                
    #                while p != count:
    #                    print"i m innnn"
    #                    count=count+1
    #                    print 'count', i.origin,count
    #                    tracking_id=stock_obj.create(cr,uid,{'name':i.origin+'_'+str(count),'date':today})
    #                    order_qty = 1
    #                    if order_qty > pack_qty:
    #                        order_qty = order_qty - pack_qty
    #                        move_qty = pack_qty
    #                    else:
    #                        move_qty = order_qty
    #
    #                    
    #                    print'package-----package',i.product_id.packaging[0].qty
    #                    print'move_qty-----move_qty',move_qty
    #                    if order_qty >= pack_qty and update_same:
    #                        update_same= False
    #                        move_obj.write(cr, uid, [i.id], {'product_qty': move_qty,'tracking_id': tracking_id}, context=context)
    #                    else:
    #                        default_val = {
    #                            'product_qty': move_qty,
    #                            'product_uos_qty': move_qty,
    #                            'tracking_id': tracking_id,
    #                            'state': i.state,
    #                            'product_uos': i.product_uom.id
    #                            }
    #                        current_move = move_obj.copy(cr, uid, i.id, default_val, context=context)
    ##        (data,)=self.browse(cr,uid,ids)
    ##        print"=====",data.move_lines
    ##        print"+++++++++++=", data.move_lines[0].id
    ##        update=move_obj.unlink(cr,uid,[data.move_lines[0].id])
    #
    ##                    val={
    ##                        'product_id':i.product_id.id,
    ##                        'product_qty':q,
    ##                        'product_uom':i.product_uom.id,
    ##                        'name':i.name,
    ##                        'location_dest_id':i.location_dest_id.id,
    ##                        'date_expected':i.date_expected,
    ##                        'location_id':i.location_id.id,
    ##                        'tracking_id':stock_id,
    ##                        'picking_id':i.picking_id.id
    ##                    }
    ##                    print i.id
    ##
    ###                    update=move_obj.unlink(cr,uid,i.id)
    ##                    product_id=move_obj.create(cr,uid,val)
    #
    #        return True
   

    # def download_label(self,cr,uid,ids,context=None):
        # for id in ids:
            
            # cr.execute(""" select label,state from stock_picking where id=%s""",(id,))
            # result = cr.fetchall()
           
            # if not (result[0][1] == 'done'):
                # raise osv.except_osv(_('Message!'),  _("Label not download because state is not Label Create !"))
        # return webbrowser.open_new_tab(result[0][0])
        
        
    # def create_shipment_simplest_2(self, cr, uid, ids, context=None):
        # postmaster.config.api_key = 'tt_NjIxMDAxOndJc2kweENoM0hOVnpfU0g1dl90X280TUJPYw'
        # sale_obj= self.pool.get('sale.order')
        # attach_object=self.pool.get('ir.attachment')
        # product_object=self.pool.get('product.product')
        # carrier_obj=self.pool.get('delivery.carrier')
        # print "============>",context
        # for data in self.browse(cr,uid,ids):
            # packages=[]
            # print "===========>",data.shipping_carrier
            # if not data.shipping_carrier:
                # if context.get('From_wizard_lable'):
                    # data.write({'faulty':True, 'error_note':'Please Enter the Shipping Carrier for order'})
                # else:
                    # raise osv.except_osv(_('Error!'),  _('Please Enter the Shipping Carrier for order  %s' % (data.name)))
    # #            raise osv.except_osv(_('Error!'),  _('Please Enter the Shipping Carrier'))
            # if data.shipping_carrier == 'fedex':
                # carrier='fedex'
                # if not data.shipping_service_fedex:
                    # if context.get('From_wizard_lable'):
                        # data.write({'faulty':True, 'error_note':'Please Enter the Shipping service'})
                    # else:
                        # raise osv.except_osv(_('Error!'),  _('Please Enter the Shipping service  for %s' % (data.name)))
                # if not data.shipping_packaging_fedex:
                    # if context.get('From_wizard_lable'):
                        # data.write({'faulty':True, 'error_note': 'Please Select the Packaging'})
                    # else:
                        # raise osv.except_osv(_('Error!'),  _('Please Select the Packaging for %s' % (data.name)))
                # packaging = data.shipping_packaging_fedex
                # service=data.shipping_service_fedex
            # elif data.shipping_carrier=='usps':
                # carrier='usps'
                # if not data.shipping_service_usps:
                    # if context.get('From_wizard_lable'):
                        # data.write({'faulty':True, 'error_note': 'Please Enter the Shipping service'})
                    # else:
                        # raise osv.except_osv(_('Error!'),  _('Please Enter the Shipping service for  %s' % (data.name)))
                # if not data.shipping_packaging_usps:
                    # if context.get('From_wizard_lable'):
                        # data.write({'faulty':True, 'error_note':'Please Select the Packaging'})
                    # else:
                        # raise osv.except_osv(_('Error!'),  _('Please Select the Packaging for %s' % (data.name)))
                # packaging = data.shipping_packaging_usps
                # service=data.shipping_service_usps
            
            # else:
                # carrier='ups'
                # if not data.shipping_service_ups:
                    # if context.get('From_wizard_lable'):
                        # data.write({'faulty':True, 'error_note': 'Please Enter the Shipping service'})
                    # else:
                        # raise osv.except_osv(_('Error!'),  _('Please Enter the Shipping service for %s' % (data.name)))
                # if not data.shipping_packaging_ups:
                    # if context.get('From_wizard_lable'):
                        # data.write({'faulty':True, 'error_note': 'Please Select the Packaging'})
                    # else:
                        # raise osv.except_osv(_('Error!'),  _('Please Select the Packaging for %s' % (data.name)))
                # packaging = data.shipping_packaging_ups
                # service= data.shipping_service_ups

            # track_no = ''
            # for move in data.move_lines:
                # if move.tracking_id:
                    # if not move.tracking_id.name==track_no:
                        
                        # if not move.product_id.packaging:
                            # if move.product_id.weight_net and move.product_id.length and move.product_id.width and move.product_id.height:
                                 # p={
                                    # 'type' :packaging,
                                    # 'weight': move.product_id.weight_net,
                                    # 'length': move.product_id.length,
                                    # 'width': move.product_id.width,
                                    # 'height': move.product_id.height,
                             # #       'customs': str(move.id),
                                  # }
                                 # packages.append(p)
                                 # p={}
                            # else:
                                # if context.get('From_wizard_lable'):
                                    # data.write({'faulty':True, 'error_note': 'Please Enter the product height,width,length,weight'})
                                # else:
                                    # raise osv.except_osv(_('Error!'),  _('Please Enter the product height,width,length,weight'))
                        # else:
                            # height = False
                            # if move.product_id.packaging[0].height:
                                # height=move.product_id.packaging[0].height
                            # else:
                                # if context.get('From_wizard_lable'):
                                    # data.write({'faulty':True, 'error_note': 'Please Enter the package height in product'})
                                # else:
                                    # raise osv.except_osv(_('Error!'),  _('Please Enter the package height in product %s' % (data.name)))
                            # length = False
                            # if move.product_id.packaging[0].length:
                                # length=move.product_id.packaging[0].length
                            # else:
                                # if context.get('From_wizard_lable'):
                                    # data.write({'faulty':True, 'error_note': 'Please Enter the package length in product'})
                                # else:
                                    # raise osv.except_osv(_('Error!'),  _('Please Enter the package length in product %s' % (data.name)))
                            # width = False
                            # if move.product_id.packaging[0].width:
                                # width=move.product_id.packaging[0].width
                            # else:
                                # if context.get('From_wizard_lable'):
                                    # data.write({'faulty':True, 'error_note': 'Please Enter the package width in product'})
                                # else:
                                    # raise osv.except_osv(_('Error!'),  _('Please Enter the package width in product %s' % (data.name)))
                            # print'move.product_id.packaging[0].weight',move.product_id.packaging[0].weight
                            # weight = False
                            # if move.product_id.packaging[0].weight:
                                # weight=move.product_id.packaging[0].weight
                            # else:
                                # if context.get('From_wizard_lable'):
                                    # data.write({'faulty':True, 'error_note': 'Please Enter the package weight in product'})
                                # else:
                                    # raise osv.except_osv(_('Error!'),  _('Please Enter the package weight in product %s' % (data.name)))
                            # p={
                                # 'type' :packaging,
                                # 'weight': weight,
                                # 'length': length,
                                # 'width': width,
                                # 'height': height,
                          # #      'customs': str(move.id),
                                # }
                            # packages.append(p)
                            # p={}
                        # print packages
                    # track_no=move.tracking_id.name

            # if data.origin:
                             
                # origin = data.origin
               # # origin = data.origin[:5]
                # sale_id=sale_obj.search(cr,uid,[('name','=',origin)])
                # sale_data=sale_obj.browse(cr,uid,sale_id)
              
                # if not sale_data[0].shop_id:
                    # if context.get('From_wizard_lable'):
                        # data.write({'faulty':True, 'error_note': 'Please Enter the Shop'})
                    # else:
                        # raise osv.except_osv(_('Error!'),  _('Please Enter the Shop %s' % (data.name)))

                # if not sale_data[0].shop_id.company_id.phone:
                    # if context.get('From_wizard_lable'):
                        # data.write({'faulty':True, 'error_note': 'Please Enter Phone number For Sender'})
                    # else:
                        # raise osv.except_osv(_('Error!'),  _('Please Enter Phone number For Sender  %s' % (data.name)))
                # if not data.partner_id.phone:
                    # if context.get('From_wizard_lable'):
                        # data.write({'faulty':True, 'error_note': 'Please Enter Phone number For Receipent'})
                    # else:
                        # raise osv.except_osv(_('Error!'),  _('Please Enter Phone number For Receipent %s' % (data.name)))

                # if not data.partner_id.street:
                    # if context.get('From_wizard_lable'):
                        # data.write({'faulty':True, 'error_note': 'Please Enter street For Receipent'})
                    # else:
                        # raise osv.except_osv(_('Error!'),  _('Please Enter street For Receipent %s' % (data.name)))
                # if not sale_data[0].shop_id.company_id.street:
                    # if context.get('From_wizard_lable'):
                        # data.write({'faulty':True, 'error_note': 'Please Enter street For Sender'})
                    # else:
                        # raise osv.except_osv(_('Error!'),  _('Please Enter street For Sender  %s' % (data.name)))


                # if not sale_data[0].shop_id.company_id.city:
                    # if context.get('From_wizard_lable'):
                        # data.write({'faulty':True, 'error_note': 'Please Enter City For Sender'})
                    # else:
                        # raise osv.except_osv(_('Error!'),  _('Please Enter City For Sender  %s' % (data.name)))
                # if not data.partner_id.city:
                    # if context.get('From_wizard_lable'):
                        # data.write({'faulty':True, 'error_note': 'Please Enter city For Receipent'})
                    # else:
                        # raise osv.except_osv(_('Error!'),  _('Please Enter city For Receipent  %s' % (data.name)))

                # if not sale_data[0].shop_id.company_id.state_id:
                    # if context.get('From_wizard_lable'):
                        # data.write({'faulty':True, 'error_note': 'Please Enter state For Sender'})
                    # else:
                        # raise osv.except_osv(_('Error!'),  _('Please Enter state For Sender  %s' % (data.name)))

                # if not data.partner_id.state_id:
                    # if context.get('From_wizard_lable'):
                        # data.write({'faulty':True, 'error_note': 'Please Enter State For Receipent'})
                    # else:
                        # raise osv.except_osv(_('Error!'),  _('Please Enter State For Receipent  %s' % (data.name)))

                # if not sale_data[0].shop_id.company_id.zip:
                    # if context.get('From_wizard_lable'):
                        # data.write({'faulty':True, 'error_note': 'Please Enter zip For Sender'})
                    # else:
                        # raise osv.except_osv(_('Error!'),  _('Please Enter zip For Sender  %s' % (data.name)))
                # if not data.partner_id.zip:
                    # if context.get('From_wizard_lable'):
                        # data.write({'faulty':True, 'error_note': 'Please Enter zip For Receipent'})
                    # else:
                        # raise osv.except_osv(_('Error!'),  _('Please Enter zip For Receipent  %s' % (data.name)))

    # #           try:
    # #           print"====>>",sale_data[0].shop_id.company_id.phone
                # to={
                    # 'company':data.partner_id.name,
                    # 'contact': data.partner_id.name,
                    # 'line1': data.partner_id.street,
                    # 'line2': data.partner_id.street2 or '',
                    # 'city': data.partner_id.city,
                    # 'state': data.partner_id.state_id.code,
                    # 'zip_code': data.partner_id.zip,
                    # 'phone_no': data.partner_id.phone,
                # }
                # packages=packages
                # carrier=str(carrier)
                # service=service
                # from_={
                    # 'contact': sale_data[0].shop_id.company_id.name,
                    # 'line1': sale_data[0].shop_id.company_id.street,
                    # 'city': sale_data[0].shop_id.company_id.city,
                    # 'state': sale_data[0].shop_id.company_id.state_id.code,
                    # 'zip_code': sale_data[0].shop_id.company_id.zip,
                    # 'phone_no': sale_data[0].shop_id.company_id.phone,
                    # }

                # try:
                    # shipment = postmaster.Shipment.create(to=to,packages=packages,carrier=carrier,service=service,from_=from_,)
    ##                   pprint(shipment._data)
                    # print  "=====shipment====>",shipment._data
                    # count=0
                    # name_label=[]
                    # image_url=False
                    # print'*********shipment.packages',shipment.packages
                    # for label in shipment.packages:
                        # image_url="https://www.postmaster.io"+label.get('label_url')
                        # print "====>",image_url
                        # if image_url:
                            # a_id = attach_object.create(cr, uid, {'name': 'Label', 'res_model': 'stock.picking.out', 'res_id': ids[0],'type': 'url', 'url': image_url})
                            # data.write({'faulty':False, 'error_note':False})
                    
                    # ################ Below code add by 4devnet to assign tracking(pack) ids to attachments(labels)############## 
                    # cr.execute('select distinct tracking_id from stock_move where picking_id=%s',(data.id,))
                    # result= cr.fetchall()
                    # i = 0
                    # for rs in result:
                        # cr.execute("select min(id) from ir_attachment where res_id=%s",(data.id,))
                        # ir_res = cr.fetchall()
                        # ir_id = ir_res[0][0] + i
                        # print str(ir_id)
                        # print 'ir_id'
                        # cr.execute('update ir_attachment set tracking_id=%s where id=%s',(rs[0],ir_id,))
                        # i += 1                  
                    # ############################################################################################################
                    
                    # carr_ids = carrier_obj.search(cr, uid, [('name','=',shipment.carrier)])
                    # carrier_code = shipment.carrier

                    # if carr_ids:
                        # c_id = carr_ids[0]
                        # #carrier_obj.write(cr, uid, c_id,{'metapack_carrier_code': carrier_code})
                    # else:
                        # prod_ids = product_object.search(cr, uid, [('type', '=','service')])
                        # if prod_ids:
                            # p_id  = prod_ids[0]
                        # else:
                            # p_id  = product_object.create(cr, uid, {'name': 'Shipping and Handling', 'type':"service" ,'categ_id': 1})
            # #            patner_id = partner_object.create(cr, uid, {'name': shipment.carrier})
                        # c_id = carrier_obj.create(cr, uid, {'name': shipment.carrier , 'metapack_carrier_code':carrier_code, 'product_id': p_id,'partner_id': sale_data[0].shop_id.company_id.id})
                    # self.write(cr,uid,ids,{'carrier_tracking_ref':shipment.tracking[0],'carrier_id':c_id})
                    # self.do_partial(cr, uid, ids, context=None)
                # except Exception, e:
                    # if context.get('From_wizard_lable'):
                        # data.write({'faulty':True, 'error_note':str(e)})
                    # else:
                        # self.write(cr,uid,ids,{'faulty':True,'error_note':e})
                        # raise osv.except_osv(('Error !'), ('%s') % (e))

                

                # '''for label in shipment.packages:
                    # print"===", label.get('label_url')
                    # count+=1
                    # image_url="https://www.postmaster.io"+label.get('label_url')
                    # file_n = "/var/www/postmaster/label_"+str(count)+('_')+str(shipment.tracking[0])+".png"

                    # file = urllib.urlopen(image_url)
                    # im = cStringIO.StringIO(file.read())
                    # img = Image.open(im)
    # #                urllib.urlretrieve(image_url, file_n)
    # #                img = Image.open(file_n)
    # #                figure = plt.gcf() # get current figure
    # #                img.drawHeight = 2*inch
    # #                img.drawWidth = 1*inch
    # #                img._restrictSize(6 * inch, 4 * inch)
    # #                im2 = img.resize((128,128),Image.ANTIALIAS)
                    # img.save("/var/www/postmaster/label_"+str(count)+('_')+str(shipment.tracking[0])+".pdf")
                    # name_label.append(str(count)+('_')+str(shipment.tracking[0])+".pdf")
        # #            file1 = "label" +str(picking.id)+".pdf"
                # print name_label
                # file1 = "/var/www/postmaster/master_"+str(shipment.tracking[0])+".pdf"
                # if os.path.exists(file1):
                    # os.remove(file1)
                # merger = PdfFileMerger()
                # #for manifest

                # for label in  name_label:
                    # print"labelssss------"
                    # child_pdf_file= "/var/www/postmaster/label_"+label
                    # print child_pdf_file
                    # merger.append(PdfFileReader(file(child_pdf_file, 'rb')))
                # merger.write(file1)'''
    # #            f=open("/var/www/master.pdf", 'rb')
            # self.write(cr, uid, ids[0], {'state': 'done'})
                
        # return True
        