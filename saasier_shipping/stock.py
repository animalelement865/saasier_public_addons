# -*- coding: utf-8 -*-
##############################################################################
#
#    OpenERP, Open Source Management Solution
#    Copyright (C) 2004-2009 Tiny SPRL (<http://tiny.be>).
#
#    This program is free software: you can redistribute it and/or modify
#    it under the terms of the GNU Affero General Public License as
#    published by the Free Software Foundation, either version 3 of the
#    License, or (at your option) any later version.
#
#    This program is distributed in the hope that it will be useful,
#    but WITHOUT ANY WARRANTY; without even the implied warranty of
#    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#    GNU Affero General Public License for more details.
#
#    You should have received a copy of the GNU Affero General Public License
#    along with this program.  If not, see <http://www.gnu.org/licenses/>.
#
##############################################################################
from datetime import datetime, date
from openerp.tools.float_utils import float_compare
import time
from openerp.osv import osv, fields
from openerp import netsvc
from tools.translate import _
import urllib
import math
import base64
import cStringIO # *much* faster than StringIO
import Image
from PyPDF2 import PdfFileMerger, PdfFileReader
import os
from pprint import pprint
import postmaster
import webbrowser
import easypost
from stamps.config import StampsConfiguration
from stamps.services import StampsService
from decimal import Decimal
from re import compile
from suds import WebFault
# from qrcode import *
#from reportlab.lib.units import mm, inch
#import matplotlib.pyplot as plt
# logger = netsvc.Logger()    NOT NEEDED ANYMORE


#Stamps Import
from time import sleep
from unittest import TestCase
import xmlrpclib

import logging
_logger = logging.getLogger(__name__)

import qrcode
from qrcode import *

caps_conversion_dict= { 'POSTCARD':'Postcard',
                        'LETTER':'Letter',
                        'LARGE ENVELOPE OR FLAT':'Large Envelope or Flat',
                        'THICK ENVELOPE':'Thick Envelope',
                        'PACKAGE':'Package',
                        'FLAT RATE BOX':'Flat Rate Box',
                        'SMALL FLAT RATE BOX':'Small Flat Rate Box',
                        'LARGE FLAT RATE BOX':'Large Flat Rate Box',
                        'FLAT RATE ENVELOPE':'Flat Rate Envelope',
                        'FLAT RATE PADDED ENVELOPE':'Flat Rate Padded Envelope',
                        'LARGE PACKAGE':'Large Package',
                        'OVERSIZED PACKAGE':'Oversized Package',
                        'REGIONAL RATE BOX A':'Regional Rate Box A',
                        'REGIONAL RATE BOX B':'Regional Rate Box B',
                        'LEGAL FLAT RATE ENVELOPE':'Legal Flat Rate Envelope',
                        'REGIONAL RATE BOX C':'Regional Rate Box C',}

class StampsService(StampsService):

    def remove_label_customised(self, transaction_id):
        """Cancel a shipping label.

        :param transaction_id: The transaction ID (or tracking number) returned
            by :meth:`get_label`.
        """
        arguments = dict(TrackingNumber=transaction_id)

        return self.call("CancelIndicium", **arguments)

    def get_label_customised_4_customs(self, from_address, to_address, rate, transaction_id, customs, memo,
            sample=False):
        """Get a shipping label.

        :param from_address: The shipping 'from' address.
        :param to_address: The shipping 'to' address.
        :param rate: A rate instance for the shipment.
        :param transaction_id: ID that may be used to retry/rollback the
            purchase of this label.
        :param customs: Label Customs.
        :param sample: Default ``False``. Get a sample label without postage.
        """
        return self.call("CreateIndicium", IntegratorTxID=transaction_id,
                Rate=rate, From=from_address, To=to_address, Customs=customs, memo=memo, printMemo=True, SampleOnly=sample)
                
    def get_label_customised(self, from_address, to_address, rate, transaction_id, memo,
            sample=False):
        """Get a shipping label.
        :param from_address: The shipping 'from' address.
        :param to_address: The shipping 'to' address.
        :param rate: A rate instance for the shipment.
        :param transaction_id: ID that may be used to retry/rollback the
            purchase of this label.
        :param sample: Default ``False``. Get a sample label without postage.
        """
        return self.call("CreateIndicium", IntegratorTxID=transaction_id,
                Rate=rate, From=from_address, To=to_address, memo=memo, printMemo=True, SampleOnly=sample)
                
    def create_customsline_wsdl(self):
        """Create a new address object.
        """
        return self.create("CustomsLine")

    def create_ArrayOfCustomsLine_wsdl(self):
        """Create a new address object.
        """
        return self.create("ArrayOfCustomsLine")

    def create_customs_wsdl(self):
        """Create a new address object. 
        """
        return self.create("CustomsV2")
        
    def GetURL_wsdl(self,URLType):
        """GetURL to get Printed labels dashboard. 
        """
        return self.call("GetURL",URLType=URLType,)
        

class res_company(osv.osv):
    
    _inherit = 'res.company'
    
    def refresh_stamps_balance(self, cr, uid, ids, context=None):
        company_record = self.browse(cr, uid, ids[0],context)
        if ( not company_record.stamps_integ_id or not company_record.stamps_username or not company_record.stamps_password):
            raise osv.except_osv(_('Message!'),  _("Please enter Integration Details for Stamps inside company !"))
        integration_id = company_record.stamps_integ_id
        username = company_record.stamps_username
        password = company_record.stamps_password
        
        if company_record.stamps_prod_vs_staging=="Production":
            stamps_configuration = StampsConfiguration(integration_id=integration_id,username=username, password=password)
        else:
            stamps_configuration = StampsConfiguration(wsdl="testing",integration_id=integration_id,username=username, password=password)
        
        stamps_service = StampsService(configuration=stamps_configuration)
        account_informations = stamps_service.get_account()
        current_balance = account_informations['AccountInfo']['PostageBalance']['AvailablePostage']
        self.write(cr, uid, ids[0], {'stamps_balance': current_balance},context)
        return current_balance
        
    def purchase_stamps_postage(self, cr, uid, ids, context=None):
        company_record = self.browse(cr, uid, ids[0],context)
        if ( not company_record.stamps_integ_id or not company_record.stamps_username or not company_record.stamps_password):
            raise osv.except_osv(_('Message!'),  _("Please enter Integration Details for Stamps inside company !"))
        integration_id = company_record.stamps_integ_id
        username = company_record.stamps_username
        password = company_record.stamps_password
        
        if company_record.stamps_prod_vs_staging=="Production":
            stamps_configuration = StampsConfiguration(integration_id=integration_id,username=username, password=password)
        else:
            stamps_configuration = StampsConfiguration(wsdl="testing",integration_id=integration_id,username=username, password=password)
        
        stamps_service = StampsService(configuration=stamps_configuration)
        
        
        
        #Purchase Balance.
        transaction_id = datetime.now().isoformat()
        amount_purchase_stamps = company_record.amount_purchase_stamps
        result = stamps_service.add_postage(amount_purchase_stamps, transaction_id=transaction_id)
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

        account_informations = stamps_service.get_account()
        current_balance = account_informations['AccountInfo']['PostageBalance']['AvailablePostage']
        self.write(cr, uid, ids[0], {'stamps_balance': current_balance},context)
        return current_balance
        
    def button_geturl(self, cr, uid, ids, context=None):
        company_record = self.browse(cr, uid, ids[0],context)
        if ( not company_record.stamps_integ_id or not company_record.stamps_username or not company_record.stamps_password):
            raise osv.except_osv(_('Message!'),  _("Please enter Integration Details for Stamps inside company !"))
        integration_id = company_record.stamps_integ_id
        username = company_record.stamps_username
        password = company_record.stamps_password
        
        if company_record.stamps_prod_vs_staging=="Production":
            stamps_configuration = StampsConfiguration(integration_id=integration_id,username=username, password=password)
        else:
            stamps_configuration = StampsConfiguration(wsdl="testing",integration_id=integration_id,username=username, password=password)
        
        stamps_service = StampsService(configuration=stamps_configuration)
                    
        #UNS
        restult_geturl = stamps_service.GetURL_wsdl('OnlineReportingHistory')
        # restult_geturl = stamps_service.GetURL_wsdl('OnlineReportsPage')
        # restult_geturl = stamps_service.GetURL_wsdl('ReportsExpenses')
        self.write(cr, uid, ids[0], {'geturl_result':str(restult_geturl['URL'])})
        return True

                
        
    _columns = {
                'easypost_prod_vs_staging' : fields.selection([('Test','Test'),('Production','Production')], 'Type of API', required=True),
                'easypost_api' : fields.char('Easypost API'),
                'usps' : fields.boolean('USPS'),
                'ups' : fields.boolean('UPS'),
                'fedex' : fields.boolean('Fedex (Not supported rightnow)'),
                'stamps_prod_vs_staging' : fields.selection([('Test','Test'),('Production','Production')], 'Type of API', required=True),
                'stamps_integ_id' : fields.char('Integration ID'),
                'stamps_username' : fields.char('Stamps Username'),
                'stamps_password' : fields.char('Stamps Password'),
                'stamps_balance' : fields.float('Stamps Balance', readonly=True),
                'amount_purchase_stamps' : fields.float('Amount to purchase',),
                'geturl_result' : fields.text('Online Reporting URL',),
                'worldship_csv' : fields.boolean('UPS WorldShip CSV'),
                'freight' : fields.boolean('Freight'),
                'datapac_csv' : fields.boolean('USPS DataPac CSV'),
                'warehouse_ids' : fields.one2many('stock.warehouse','company_id','Warehouses',),
                'label_template_id' : fields.many2one('ir.actions.report.xml','Label Template', required=True, domain=[('model','=','stock.picking.out.label')]),
                
                'ups_third_party_company_id' : fields.many2one('res.company','UPS-Third party company provider'),
                'ups_third_party_account' : fields.char('UPS-Third party Account Number'),
                'ups_third_party_country_id' : fields.many2one('res.country', 'UPS-Third party country'),
                'ups_third_party_postal_code' : fields.char('UPS-Third party postal code'),

                }
                
    def _check_at_least_one_warehouse(self, cr, uid, ids, context=None):
        if len(self.browse(cr, uid, ids[0]).warehouse_ids)==0:
            raise osv.except_osv(_('Error'), _('You must at least add one warehouse per company.\n Look at "Shipping Configuration" Page.'))
        return True
    
    _constraints = [
        (_check_at_least_one_warehouse,
            'You must at least add one warehouse per company.\n Look at "Shipping Configuration" Page.',
            ['warehouse_ids']),]
            
res_company()

class res_partner(osv.osv):
    _inherit = 'res.partner'
    
    _columns = {
        'address_validation' : fields.boolean('Address Validation'),
    }
    
    _defaults = {
        'address_validation' : True,
    }
res_partner()

class ir_attachment(osv.osv):
    
    _inherit = "ir.attachment"
    _columns = {
                'tracking_id' : fields.integer('Tracking Id'),
                'name' : fields.text('Attachment Name'),
                }
ir_attachment()


class stock_warehouse(osv.osv):

    _inherit = 'stock.warehouse'

    _columns = {
                    'ftp' : fields.boolean('FTP'),
                    'server_add' : fields.char('Server Address', size=25),
                    'username' : fields.char('Username', size=25),
                    'pass' : fields.char('Password', size=25),
                    'g_drive' : fields.boolean('Google Drive'),
                    'email_check' : fields.boolean('Email'),
                    'email': fields.char('Email address to send shipping label to', size=240),
                    'folder_name' : fields.char('Folder'),
                    'partner_id': fields.many2one('res.partner', 'Owner Address',required=True),
                }
stock_warehouse()

class stock_location(osv.osv): 
    _inherit = "stock.location"
   
    _columns = {
        'partner_id': fields.many2one('res.partner', 'Location Address',required=True),
    }
stock_location()

class box_setup(osv.osv):

    _name = 'box.setup'
    _description = 'Box Setup'
    _columns = {
                    'name' : fields.char('Box Name',required=True),
                    'height' : fields.float('Height'),
                    'width' : fields.float('Width'),
                    'length' : fields.float('Length'),
                    'weight' : fields.float('Weight'),
                    'p_height' : fields.float('Maximum Pallet Height'),
                    'maximum_weight' : fields.float('Maximum Weight'),
                    'box_type' : fields.selection([('unit','Unit'),('pack','Pack'),('box','Box'),('pallet','Pallet')]),
                }
box_setup()


class stock_picking_out_label(osv.osv):

    state_label_selection = [
                        ('done', 'Label Created'),
                        ('available_for_pickup','Available for Pickup'),
                        ('label-sent', 'Label Sent'),
                        ('pre_transit', 'Pre Transit'),
                        ('in_transit', 'In Transit'),
                        ('out_for_delivery', 'Out for Delivery'),
                        ('delivered', 'Delivered'),
                        ('label-printed', 'Label Printed'),
                        ('cancel', 'Cancelled'),
                        ('exception', 'Exception'),]
                           
    _name = "stock.picking.out.label"
    _description = "Label container of delivery orders" 
    _order = "id desc"

    def _get_label_state(self, cr, uid, ids, name, args, context=None):
        res = {}
        state_label_list = ['exception','cancel','done','available_for_pickup','label-sent','pre_transit','in_transit','out_for_delivery','delivered',]
        for pick_label in self.browse(cr, uid, ids, context=context):
            state = ''
            for pick in pick_label.picking_ids:
                #Get the baddest State of delivery orders, and put it in 'state' var, so that it will be the state of the whole picking.label
                try:
                    if state=='':
                        state = pick.state
                    elif state_label_list.index(pick.state) > state_label_list.index(state):
                        state = pick.state
                except ValueError:
                    if state=='':
                        state='exception'

            if state=='available_for_pickup':
                state='delivered'

                #After all, if the Label was already printed, just ignore the baddest state of deliveries and put instead of it 'label-printed'
            if pick_label.is_label_printed:
                state = 'label-printed'

            res[pick_label.id] =  state
        return res
    
    _columns = {
        'picking_ids' : fields.many2many('stock.picking.out', 'stock_picking_label_ids', 'label_id', 'picking_id', 'Shipments', readonly=False),
        'location_id' : fields.many2one('stock.location', 'Location', readonly=True),
        'date' : fields.datetime('Creation Date', readonly=True),
        'state': fields.function(_get_label_state, type="selection", selection=state_label_selection, string='Status', store=False),
        'is_label_created': fields.boolean('Is Label Created ?'),
        'is_label_printed': fields.boolean('Is Label Printed ?'),
        'label_template_id' : fields.many2one('ir.actions.report.xml','Label Template', domain=[('model','=','stock.picking.out.label')]),
        'labels_type' : fields.selection([('Label Template','Label Template'),('Freight','Freight'),('UPS WorldShip CSV','UPS WorldShip CSV'),('USPS DataPac CSV','USPS DataPac CSV')],'Labels Type')
    }
    
    def print_label_template(self, cr, uid, ids, context=None):
        report_obj = self.pool.get('ir.actions.report.xml')
        datas = {}
        if context is None:
            context = {}
        data = self.read(cr, uid, ids)[0]
        datas = {
                     'ids': [ids[0]],
                     'model': 'stock.picking.out.label',
                     'form': data,
        }
        #Get the name of the report to use from label_template_id field
        rep_to_use = report_obj.browse(cr, uid, data['label_template_id'][0], context)
        return {'type': 'ir.actions.report.xml', 'report_name': rep_to_use.report_name, 'datas': datas}
stock_picking_out_label()



class stock_production_lot(osv.osv):
    
    _inherit = 'stock.production.lot'
    _order = "id desc"

    _columns = {
    }
stock_production_lot()

class shipping_calculator(osv.osv):
    
    _name = 'shipping.calculator'
    
    _columns = {
                'name' : fields.char('Method'),
                'service' : fields.char('Service'),
                'rate' : fields.float('Rate'),
                'picking_id' : fields.many2one('stock.picking','Picking Id'),
                'rate_description' : fields.char('Rate Carrier'),
                'tracking_id' : fields.many2one('stock.tracking','Pack')
                }
shipping_calculator()

class email_template(osv.osv):
    "Templates for sending email"
    _inherit = 'email.template'
    _columns = {
    }
    def send_mail(self, cr, uid, template_id, res_id, force_send=False, context=None):
        """Generates a new mail message for the given template and record,
           and schedules it for delivery through the ``mail`` module's scheduler.

           :param int template_id: id of the template to render
           :param int res_id: id of the record to render the template with
                              (model is taken from the template)
           :param bool force_send: if True, the generated mail.message is
                immediately sent after being created, as if the scheduler
                was executed for this message only.
           :returns: id of the mail.message that was created
        """
        if context is None:
            context = {}
        mail_mail = self.pool.get('mail.mail')
        ir_attachment = self.pool.get('ir.attachment')

        # create a mail_mail based on values, without attachments
        values = self.generate_email(cr, uid, template_id, res_id, context=context)
        if not values.get('email_from'):
            values['email_from'] =  self.pool.get('res.users').browse(cr,uid,uid,context=context).email
            if not values['email_from']:
                raise osv.except_osv(_('Warning!'),_("Sender email is missing or empty after template rendering. Specify one to deliver your message"))
        # process email_recipients field that is a comma separated list of partner_ids -> recipient_ids
        # NOTE: only usable if force_send is True, because otherwise the value is
        # not stored on the mail_mail, and therefore lost -> fixed in v8
        recipient_ids = []
        values['email_to'] = ''
        email_recipients = values.pop('email_recipients', '')
        if email_recipients:
            for partner_id in email_recipients.split(','):
                if partner_id:  # placeholders could generate '', 3, 2 due to some empty field values
                    recipient_ids.append(int(partner_id))
                    recepient_partner_rec = self.pool.get('res.partner').browse(cr, uid, int(partner_id))
                    email_item = recepient_partner_rec.email
                    if not email_item:
                        if recepient_partner_rec.parent_id:
                            email_item = recepient_partner_rec.parent_id.email
                        else:
                            _logger.warning('/!\ SaaSier Mailing /!\ Couldn\'t set mail receiver for customer : %s . with ID : %s', (recepient_partner_rec.name, recepient_partner_rec.id))

                    if email_item:
                        values['email_to'] += values['email_to'] + str(email_item) + ','

        attachment_ids = values.pop('attachment_ids', [])
        attachments = values.pop('attachments', [])
        msg_id = mail_mail.create(cr, uid, values, context=context)
        mail = mail_mail.browse(cr, uid, msg_id, context=context)

        # manage attachments
        for attachment in attachments:
            attachment_data = {
                'name': attachment[0],
                'datas_fname': attachment[0],
                'datas': attachment[1],
                'res_model': 'mail.message',
                'res_id': mail.mail_message_id.id,
            }
            context.pop('default_type', None)
            attachment_ids.append(ir_attachment.create(cr, uid, attachment_data, context=context))
        if attachment_ids:
            values['attachment_ids'] = [(6, 0, attachment_ids)]
            mail_mail.write(cr, uid, msg_id, {'attachment_ids': [(6, 0, attachment_ids)]}, context=context)

        if force_send:
            mail_mail.send(cr, uid, [msg_id], recipient_ids=recipient_ids, context=context)
        return msg_id
email_template()

class stock_tracking(osv.osv):
    
    _name = "stock.tracking"
    _inherit = 'stock.tracking'
    _columns = {
                    'box_setup_id' : fields.many2one('box.setup','Box Setup'),
                    'content_type' : fields.selection([('Commercial Sample','Commercial Sample'),
                                                                      ('Gift','Gift'),
                                                                      ('Document','Document'),
                                                                      ('Merchandise','Merchandise'),
                                                                      ('Returned Goods','Returned Goods'),
                                                                      ('Other','Other')],
                                                                     'Content Type'),
                    'other_describe' : fields.char('Other Describe',),
                }
stock_tracking()

class stock_move(osv.osv):
        
    _inherit = "stock.move"
    
    def _cal_move_weight(self, cr, uid, ids, name, args, context=None):
        res = {}
        uom_obj = self.pool.get('product.uom')
        for move in self.browse(cr, uid, ids, context=context):
            weight = weight_net = 0.00
            # converted_qty = move.product_qty
            # if move.product_uom.id <> move.product_id.uom_id.id:
                # converted_qty = uom_obj._compute_qty(cr, uid, move.product_uom.id, move.product_qty, move.product_id.uom_id.id)
            weight = (move.product_qty * move.product_id.weight_net)
            if move.tracking_id:
                if move.tracking_id.box_setup_id:
                    weight = weight + move.tracking_id.box_setup_id.weight
            elif move.product_id.packaging:
                weight = weight + move.product_id.packaging[0].weight_ul
            # if move.product_id.weight_net > 0.00:
            # weight_net = (move.product_qty * move.product_id.weight_net)
            res[move.id] =  weight
        return res
   
    def _get_is_state_changed(self, cr, uid, ids, name, args, context=None):
        res={}
        for move in self.browse(cr, uid, ids, context):
            if move.last_status_emailed==move.label_state:
                res[move.id] =  False
            else:
                res[move.id] =  True
        return res

    _columns = {
        'insured':fields.boolean('Insure Shipping ?'),
        'value_insured':fields.float('Value Insured'),
        'refund_confirmation_number':fields.char('Refund Confirmation Number',size=100,readonly=True),
        'refund_status':fields.char('Refund Status',size=100,readonly=True),
        'refund_id':fields.char('Refund ID',size=100,readonly=True),
        'override_weight': fields.function(_cal_move_weight, type='float', string='Weight',store=False),
        'carrier_tracking_ref' : fields.char('Carrier Tracking Ref',size=100),
        'exception_description':fields.text('EasyPost Exception',readonly=True),
        'label_state': fields.selection([
                                   ('exception', 'Exception'), #This includes : unknown, return_to_sender, failure -> details are in exception_description field
                                   ('cancel', 'Cancelled'),
                                   ('available_for_pickup', 'Available for Pickup'),
                                   ('pre_transit', 'Pre Transit'),
                                   ('in_transit', 'In Transit'),
                                   ('out_for_delivery', 'Out for Delivery'),
                                   ('delivered', 'Delivered'),                                        
                                   ], 'Shipping Status', readonly=True, select=True,),
        'error_note':fields.text('Error Note'),
        'faulty':fields.boolean('Faulty'),
        'url_tracking_ref':fields.char('Web Tracking URL'),
        'last_status_emailed' : fields.text('Last Status Emailed'),
        'is_state_changed': fields.function(_get_is_state_changed,type='boolean', store=True,string='Is State Updated with EasyPost Tracker ?'),
    }
    
stock_move()

#This override the do_partial method of parial.picking object, which have a weird official bug on it !
class stock_partial_picking(osv.osv_memory):
    _name = "stock.partial.picking"
    _inherit = 'stock.partial.picking'

    def do_partial(self, cr, uid, ids, context=None):
        assert len(ids) == 1, 'Partial picking processing may only be done one at a time.'
        stock_picking = self.pool.get('stock.picking')
        stock_move = self.pool.get('stock.move')
        uom_obj = self.pool.get('product.uom')
        partial = self.browse(cr, uid, ids[0], context=context)
        partial_data = {
            'delivery_date' : partial.date
        }
        picking_type = partial.picking_id.type
        for wizard_line in partial.move_ids:
            line_uom = wizard_line.product_uom
            move_id = wizard_line.move_id.id

            #Quantiny must be Positive
            if wizard_line.quantity < 0:
                raise osv.except_osv(_('Warning!'), _('Please provide proper Quantity.'))

            #Compute the quantity for respective wizard_line in the line uom (this jsut do the rounding if necessary)
            qty_in_line_uom = uom_obj._compute_qty(cr, uid, line_uom.id, wizard_line.quantity, line_uom.id)

            if line_uom.factor and line_uom.factor <> 0:
                if float_compare(qty_in_line_uom, wizard_line.quantity, precision_rounding=line_uom.rounding) != 0:
                    raise osv.except_osv(_('Warning!'), _('The unit of measure rounding does not allow you to ship "%s %s", only rounding of "%s %s" is accepted by the Unit of Measure.') % (wizard_line.quantity, line_uom.name, line_uom.rounding, line_uom.name))
            if move_id:
                #Check rounding Quantity.ex.
                #picking: 1kg, uom kg rounding = 0.01 (rounding to 10g),
                #partial delivery: 253g
                #=> result= refused, as the qty left on picking would be 0.747kg and only 0.75 is accepted by the uom.
                initial_uom = wizard_line.move_id.product_uom
                #Compute the quantity for respective wizard_line in the initial uom
                qty_in_initial_uom = uom_obj._compute_qty(cr, uid, line_uom.id, wizard_line.quantity, initial_uom.id)
                without_rounding_qty = (wizard_line.quantity / line_uom.factor) * initial_uom.factor
                if float_compare(qty_in_initial_uom, without_rounding_qty, precision_rounding=initial_uom.rounding) != 0:
                    raise osv.except_osv(_('Warning!'), _('The rounding of the initial uom does not allow you to ship "%s %s", as it would let a quantity of "%s %s" to ship and only rounding of "%s %s" is accepted by the uom.') % (wizard_line.quantity, line_uom.name, wizard_line.move_id.product_qty - without_rounding_qty, initial_uom.name, initial_uom.rounding, initial_uom.name))
            else:
                seq_obj_name =  'stock.picking.' + picking_type
                move_id = stock_move.create(cr,uid,{'name' : self.pool.get('ir.sequence').get(cr, uid, seq_obj_name),
                                                    'product_id': wizard_line.product_id.id,
                                                    'product_qty': wizard_line.quantity,
                                                    'product_uom': wizard_line.product_uom.id,
                                                    'prodlot_id': wizard_line.prodlot_id.id,
                                                    'location_id' : wizard_line.location_id.id,
                                                    'location_dest_id' : wizard_line.location_dest_id.id,
                                                    'picking_id': partial.picking_id.id
                                                    },context=context)
                stock_move.action_confirm(cr, uid, [move_id], context)
            partial_data['move%s' % (move_id)] = {
                'product_id': wizard_line.product_id.id,
                'product_qty': wizard_line.quantity,
                'product_uom': wizard_line.product_uom.id,
                'prodlot_id': wizard_line.prodlot_id.id,
            }
            if (picking_type == 'in') and (wizard_line.product_id.cost_method == 'average'):
                partial_data['move%s' % (wizard_line.move_id.id)].update(product_price=wizard_line.cost,
                                                                  product_currency=wizard_line.currency.id)
        #Uns Commented this because of a weird error
        #stock_picking.do_partial(cr, uid, [partial.picking_id.id], partial_data, context=context)
        stock_picking.do_partial(cr, uid, [partial.picking_id.id], partial_data)
	return {'type': 'ir.actions.act_window_close'}
stock_partial_picking()

class stock_picking_in(osv.osv):
    _inherit = "stock.picking.in"
    
    def replaced_draft_force_assign(self, cr, uid, ids, context=None):
        return self.pool.get('stock.picking').replaced_draft_force_assign(cr, uid, ids, context=context)
stock_picking_in()

class stock_picking_out(osv.osv):
    _inherit = "stock.picking.out"
    
    def _cal_total_weight(self, cr, uid, ids, name, args, context=None):
        return self.pool.get('stock.picking')._cal_total_weight(cr, uid, ids, name, args, context=context)
        
    def _cal_two_days_old(self, cr, uid, ids, name, args, context=None):
        return self.pool.get('stock.picking')._cal_two_days_old(cr, uid, ids, name, args, context=context)
        
    def _cal_seven_days_old(self, cr, uid, ids, name, args, context=None):
        return self.pool.get('stock.picking')._cal_seven_days_old(cr, uid, ids, name, args, context=context)

    def _get_shipping_carrier(self, cr, uid, context=None):
        return self.pool.get('stock.picking')._get_shipping_carrier(cr, uid, ids, name, args, context=context)
        
    _columns = {
        'state': fields.selection(
            [('draft', 'Draft'),
            ('auto', 'Waiting Another Operation'),
            ('confirmed', 'Not processed'),
            ('assigned', 'Ready to Process'),
            ('ship_queue', 'Shipping Queue'),
            ('done', 'Label Created'),
            ('available_for_pickup','Available for Pickup'),
            ('label-sent', 'Label Sent'),
            ('pre_transit', 'Pre Transit'),
            ('in_transit', 'In Transit'),
            ('out_for_delivery', 'Out for Delivery'),
            ('return_to_sender', 'Return to Sender'),
            ('failure', 'Failure'),
            ('unknown', 'Unknown'),
            ('exception', 'Exception'),
            ('cancel', 'Cancelled'),
            ('delivered', 'Delivered'),            
            ],
            'Status', readonly=True, select=True,
            help="""* Draft: not confirmed yet and will not be scheduled until confirmed\n
                 * Waiting Another Operation: waiting for another move to proceed before it becomes automatically available (e.g. in Make-To-Order flows)\n
                 * Waiting Availability: still waiting for the availability of products\n
                 * Ready to Process: products reserved, simply waiting for confirmation.\n
                 * Delivered: has been processed, can't be modified or cancelled anymore\n
                 * Cancelled: has been cancelled, can't be confirmed anymore"""),
        'shipping_carrier':fields.selection([('UPS','UPS'),('FEDEX','FEDEX'),
                                            ('USPS','USPS'),('STAMPS','STAMPS'),
                                            ('UPS WorldShip CSV','UPS WorldShip CSV'),('USPS DataPac CSV','USPS DataPac CSV'),
                                            ('Freight','Freight')],
                                            'Shipping Carrier'),
        # 'shipping_carrier':fields.selection(_get_shipping_carrier,'Shipping Carrier'),
        'shipping_service_freight' : fields.char('Shipping Freight Service'),
        'shipping_service_fedex':fields.selection([('FEDEX_GROUND', 'Ground Commercial'),
                                   ('FEDEX_2_DAY', '2Day'),
                                   ('FEDEX_2_DAY_AM', '2Day AM'),
                                   ('FEDEX_EXPRESS_SAVER', 'Express Saver'),
                                   ('STANDARD_OVERNIGHT', 'Standard Overnight'),
                                   ('FIRST_OVERNIGHT', 'First Overnight'),
                                   ('PRIORITY_OVERNIGHT', 'Priority Overnight'),
                                   ('INTERNATIONAL_ECONOMY', 'International Economy'),
                                   ('INTERNATIONAL_FIRST', 'International First'),
                                   ('INTERNATIONAL_PRIORITY', 'International Priority'), 
                                   ('GROUND_HOME_DELIVERY', 'Ground Home'),
                                   ('SMART_POST', 'Smart Post'),
                                   ], 'Shipping Service Fedex'),
        'shipping_service_ups':fields.selection([('GROUND', 'Ground'),
                                   ('UPSSTANDARDS','UPS Standards'),
                                   ('UPSSAVER','UPS Saver'),
                                   ('EXPRESS','Express'),
                                   ('EXPRESSPLUS','Express Plus'),
                                   ('EXPEDITED','Expedited'),
                                   ('NEXTDAYAIR', 'Next Day Air'),
                                   ('NEXTDAYAIRSAVER', 'Next Day Air Saver'),
                                   ('NEXTDAYAIREARLYAM', 'Next Day Air Early A.M.'),
                                   ('2NDDAYAIR', '2nd Day Air'),
                                   ('2NDDAYAIRAM', '2nd Day Air AM'),
                                   ('3DAYSELECT', '3 Day Select'),
                                   ('SUREPOST - SUREPOSTUNDER1LB','Sure Post - Sure Post Under 1Lb'),
                                   ('SUREPOST - SUREPOSTOVER1LB','Sure Post - Sure Post Over 1Lb'),
                                   ('MI - FIRST','MI - First'),
                                   ('MI - PRIORITYMAIL','MI - Priority Mail'),
                                   ('MI - EXPEDITEDMAILINNOVATIONS','MI - Expedited Mail Innovations'),
                                   ('MI - PRIORITYMAILINNOVATIONS','MI - Priority Mail Innovations'),
                                   ('MI - ECONOMYMAILINNOVATIONS','MI - Economy Mail Innovations'),
                                   ], 'Shipping Service UPS'),
        'shipping_service_usps':fields.selection([('FIRST', 'First'),
                                   ('PRIORITY', 'Priority'),
                                   ('EXPRESS', 'Express'),
                                   ('PARCELSELECT', 'Parcel Select'),
                                   ('LIBRARYMAIL ', 'Library Mail'),
                                   ('MEDIAMAIL', 'Media Mail'),
                                   ('CRITICALMAIL', 'Critical Mail'),
                                   ('FIRSTCLASSMAILINTERNATIONAL', 'First Class Mail - International'),
                                   ('FIRSTCLASSPACKAGEINTERNATIONAL', 'First Class Package - International'),
                                   ('PRIORITYMAILINTERNATIONAL', 'Priority Mail International'),
                                   ('EXPRESSMAILINTERNATIONAL', 'Express Mail International'),
                                   ], 'Shipping Service USPS'),
        'shipping_service_usps_stamps':fields.selection([('US-FC', 'USPS First-Class Mail'),
                                   ('US-PM', 'USPS Priority Mail'),
                                   ('US-XM', 'USPS Express Mail'),
                                   ('US-MM ', 'USPS Media Mail'),
                                   ('US-PP', 'USPS Parcel Post'),
                                   ('US-LM', 'USPS Library Mail'),
                                   ('US-EMI', 'USPS Express Mail International'),
                                   ('US-PMI', 'USPS Priority Mail International'),
                                   ('US-FCI', 'USPS First Class Mail International'),
                                   ('US-CM', 'USPS Critical Mail'),
                                   ('US-PS', 'USPS Parcel Select'),
                                   ], 'Shipping Service USPS STAMPS'),
        # 'shipping_service_lso':fields.selection([
                                           # ('1DAY_MORNING', 'Priority Basic'),
                                           # ], 'Shipping Service LSO'
                                          # ),
        # 'shipping_service_canada_post':fields.selection([
                                           # ('1DAY_MORNING', 'Priority Basic'),
                                           # ], 'Shipping Service Canada Post'
                                          # ),
        'shipping_packaging_fedex':fields.selection([('FEDEXENVELOPE', 'Fedex Envelope'),
                                      ('FEDEXBOX', 'Fedex Box'),
                                      ('FEDEXPAK', 'Fedex Pak'),
                                      ('FEDEXTUBE', 'Fedex Tube'),   
                                      ('FEDEX10KGBOX', 'Fedex 10 kg Box'),  
                                      ('FEDEX25KGBOX', 'Fedex 25 kg Box'),
                                      ], 'Shipping packaging Fedex'),
        'shipping_packaging_ups':fields.selection([('UPSLETTER', 'UPS Letter'),
                                     ('UPSEXPRESSBOX', 'UPS Express Box'),
                                     ('UPS25KGBOX', 'UPS 25 kg Box'),
                                     ('UPS10KGBOX', 'UPS 10 kg Box'),
                                     ('TUBE', 'UPS Tube'),
                                     ('PAK', 'UPS PAK'),
                                     ('PALLET', 'UPS Pallet'),
                                     ('SMALLEXPRESSBOX', 'UPS Small Box'),
                                     ('MEDIUMEXPRESSBOX', 'UPS Medium Box'),
                                     ('LARGEEXPRESSBOX', 'UPS Large Box'),
                                    ], 'Shipping Packaging UPS'),
        'shipping_packaging_usps':fields.selection([('CARD','USPS Card'),
                                      ('LETTER', 'Letter'),
                                      ('FLAT','Flat'),
                                      ('PARCEL', 'Parcel'),
                                      ('LARGEPARCEL', 'Large Parcel'),
                                      ('IRREGULARPARCEL', 'Irregular Parcel'),
                                      ('FLATRATEENVELOPE', 'Flat Rate Envelope'),
                                      ('FLATRATELEGALENVELOPE', 'Flat Rate Legal Envelope'),
                                      ('FLATRATEPADDEDENVELOPE', 'Flat Rate Padded Envelope'),
                                      ('FLATRATEGIFTCARDENVELOPE', 'Flat Rate Gift Card Envelope'),
                                      ('FLATRATEWINDOWENVELOPE', 'Flat Rate Window Envelope'),
                                      ('FLATRATECARDBOARDENVELOPE', 'Flat Rate Cardboard Envelope'),
                                      ('SMALLFLATRATEENVELOPE', 'Small Flat Rate Envelope'),
                                      ('SMALLFLATRATEBOX', 'Small Flat Rate Box'),
                                      ('MEDIUMFLATRATEBOX', 'Medium Flat Rate Box'),
                                      ('LARGEFLATRATEBOX', 'Large Flat Rate Box'), 
                                      ('REGIONALRATEBOXA', 'Regional Rate A'),
                                      ('REGIONALRATEBOXB', 'Regional Rate B'),
                                      ('REGIONALRATEBOXC', 'Regional Rate C'),
                                      ('LARGEFLATRATEBOARDGAMEBOX', 'Large Flat Rate Board Game Box'),
                                     ], 'Shipping Packaging USPS'), 
        'shipping_packaging_usps_stamps':fields.selection([('POSTCARD','Postcard'),
                                      ('LETTER', 'Letter'),
                                      ('LARGE ENVELOPE OR FLAT','Large Envelope or Flat'),
                                      ('THICK ENVELOPE', 'Thick Envelope'),
                                      ('PACKAGE', 'Package'),
                                      ('FLAT RATE BOX', 'Flat Rate Box'),
                                      ('SMALL FLAT RATE BOX', 'Small Flat Rate Box'),
                                      ('MEDIUM FLAT RATE BOX', 'Medium Flat Rate Box'),
                                      ('LARGE FLAT RATE BOX', 'Large Flat Rate Box'),
                                      ('FLAT RATE ENVELOPE', 'Flat Rate Envelope'),
                                      ('FLAT RATE PADDED ENVELOPE', 'Flat Rate Padded Envelope'),
                                      ('LARGE PACKAGE', 'Large Package'),
                                      ('OVERSIZED PACKAGE', 'Oversized Package'),
                                      ('REGIONAL RATE BOX A', 'Regional Rate Box A'),
                                      ('REGIONAL RATE BOX B', 'Regional Rate Box B'),
                                      ('LEGAL FLAT RATE ENVELOPE', 'Legal Flat Rate Envelope'),
                                      ('REGIONAL RATE BOX C', 'Regional Rate Box C'),
                                     ], 'Shipping Packaging USPS STAMPS'), 
        'exception_description':fields.text('EasyPost Exception',readonly=True),
        'total_weight': fields.function(_cal_total_weight, type='float', string='Total Weight',store=False),
        'two_days_old': fields.function(_cal_two_days_old, type='integer', string='2 Days Old',store=False),
        'seven_days_old': fields.function(_cal_seven_days_old, type='integer', string='7 Days Old',store=False),
        'insured':fields.boolean('Insure Shipping ?'),
        'value_insured':fields.float('Value Insured'),
        'refund_confirmation_number':fields.char('Refund Confirmation Number',size=100,readonly=True),
        'refund_status':fields.char('Refund Status',size=100,readonly=True),
        'refund_id':fields.char('Refund ID',size=100,readonly=True),
        'batch_no':fields.char('Batch No',size=100),
        'error_note':fields.text('Error Note'),
        'faulty':fields.boolean('Faulty'),
        'url_tracking_ref':fields.char('Web Tracking URL'),
        'label': fields.text('Url Label'),
        'list_tracking_numbers': fields.text('List of Tracking Numbers'),
        'ship_cal_id' : fields.one2many('shipping.calculator','picking_id','Rate Line'),       
    }

    def replaced_draft_force_assign(self, cr, uid, ids, context=None):
        return self.pool.get('stock.picking').replaced_draft_force_assign(cr, uid, ids, context=context)


    def packaging_product(self, cr, uid, ids, context=None):
        return self.pool.get('stock.picking').packaging_product(cr, uid, ids, context=context)
    

    def get_shipment_rate(self, cr, uid, ids, context=None): 
        return self.pool.get('stock.picking').get_shipment_rate(cr, uid, ids, context=context)


    def add_to_ship_que(self, cr, uid, ids, context=None):
        return self.pool.get('stock.picking').add_to_ship_que(cr, uid, ids, context=context)

    def remove_from_ship_que(self, cr, uid, ids, context=None):
        return self.pool.get('stock.picking').remove_from_ship_que(cr, uid, ids, context=context)


    def split_order_wr(self, cr, uid, ids, context=None):
        return self.pool.get('stock.picking').split_order_wr(cr, uid, ids, context=context)


    def cancel_shipment(self, cr, uid, ids, context=None):
        return self.pool.get('stock.picking').cancel_shipment(cr, uid, ids, context=context)


    def send_to_warehouse(self,cr,uid,ids,context=None):   
        return self.pool.get('stock.picking').send_to_warehouse(cr, uid, ids, context=context)

        
    def get_stamps_address(self, service, FullName, Address1, Address2, City, State, ZIPCode, Country, Phone):
        return self.pool.get('stock.picking').get_stamps_address(service, FullName, Address1, Address2, City, State, ZIPCode, Country, Phone)

    def create_customsline(self, service, Description,Quantity, Value,WeightLb,HSTariffNumber,CountryOfOrigin):
        return self.pool.get('stock.picking').create_customsline(service, Description,Quantity, Value,WeightLb,HSTariffNumber,CountryOfOrigin)
        
    def create_customs(self, service, ContentType, OtherDescribe,CustomsLines):
        return self.pool.get('stock.picking').create_customs(service, ContentType, OtherDescribe,CustomsLines)
        
    def cron_create_shipment_simplest(self, cr, uid, ids, context=None):
        return self.pool.get('stock.picking').cron_create_shipment_simplest(cr, uid, ids, context=context)
        
    def create_shipment_simplest(self, cr, uid, ids, context=None):
        return self.pool.get('stock.picking').create_shipment_simplest(cr, uid, ids, context=context)

    def update_label_status(self, cr, uid, ids, context=None):
        return self.pool.get('stock.picking').update_label_status(cr, uid, ids, context=context)
        
    def send_email_tracking(self, cr, uid, ids, context=None):
          return self.pool.get('stock.picking').send_email_tracking(cr, uid, ids, context=context)

    def send_email_new_state(self, cr, uid, ids, context=None):
        return self.pool.get('stock.picking').send_email_new_state(cr, uid, ids, context=context)

    def check_new_state_and_email(self, cr, uid, ids, context=None):
        return self.pool.get('stock.picking').check_new_state_and_email(cr, uid, ids, context=context)
        
stock_picking_out()
    
class stock_picking(osv.osv):
    _name = "stock.picking"
    _inherit = "stock.picking"
    
    def _cal_total_weight(self, cr, uid, ids, name, args, context=None):
        res = {}
        uom_obj = self.pool.get('product.uom')
        for pick in self.browse(cr, uid, ids, context=context):
            weight = 0.00
            for move in pick.move_lines:
                weight += move.override_weight
            res[pick.id] =  weight
        return res
        
    def _cal_two_days_old(self, cr, uid, ids, name, args, context=None):
        res = {}
        for pick in self.browse(cr, uid, ids, context=context):
            if not pick.date:
                res[pick.id] = 1
                continue
            delta = datetime.now() - datetime.strptime(pick.date, '%Y-%m-%d %H:%M:%S')
            if delta.days >= 2:
                res[pick.id] = 1
            else:
                res[pick.id] = 0
        return res
        
    def _cal_seven_days_old(self, cr, uid, ids, name, args, context=None):
        res = {}
        for pick in self.browse(cr, uid, ids, context=context):
            if not pick.date:
                res[pick.id] = 1
                continue
            delta = datetime.now() - datetime.strptime(pick.date, '%Y-%m-%d %H:%M:%S')
            if delta.days >= 7:
                res[pick.id] = 1
            else:
                res[pick.id] = 0
        return res

    def _get_shipping_carrier(self, cr, uid, context=None):
        res = []
        user_obj = self.pool.get('res.users').browse(cr, uid, uid, context)
        if user_obj.company_id.usps and user_obj.company_id.easypost_api:
            res.append(('USPS','USPS'))
        if user_obj.company_id.ups and user_obj.company_id.easypost_api:
            res.append(('UPS','UPS'))
        if user_obj.company_id.fedex and user_obj.company_id.easypost_api:
            res.append(('FEDEX','FEDEX'))
        if user_obj.company_id.stamps_integ_id and user_obj.company_id.stamps_username and user_obj.company_id.stamps_password:
            res.append(('STAMPS','STAMPS'))
        if user_obj.company_id.worldship_csv:
            res.append(('UPS WorldShip CSV','UPS WorldShip CSV'))
        if user_obj.company_id.datapac_csv:
            res.append(('USPS DataPac CSV','USPS DataPac CSV'))
        if user_obj.company_id.freight:
            res.append(('Freight','Freight'))
        return res
        

    _columns = {
        'state': fields.selection(
            [('draft', 'Draft'),
            ('auto', 'Waiting Another Operation'),
            ('confirmed', 'Not processed'),
            ('assigned', 'Ready to Process'),
            ('ship_queue', 'Shipping Queue'),
            ('done', 'Label Created'),
            ('available_for_pickup','Available for Pickup'),
            ('label-sent', 'Label Sent'),
            ('pre_transit', 'Pre Transit'),
            ('in_transit', 'In Transit'),
            ('out_for_delivery', 'Out for Delivery'),
            ('return_to_sender', 'Return to Sender'),
            ('failure', 'Failure'),
            ('unknown', 'Unknown'),
            ('exception', 'Exception'),
            ('cancel', 'Cancelled'),
            ('delivered', 'Delivered'),            
            ],
            'Status', readonly=True, select=True,
            help="""* Draft: not confirmed yet and will not be scheduled until confirmed\n
                 * Waiting Another Operation: waiting for another move to proceed before it becomes automatically available (e.g. in Make-To-Order flows)\n
                 * Waiting Availability: still waiting for the availability of products\n
                 * Ready to Process: products reserved, simply waiting for confirmation.\n
                 * Delivered: has been processed, can't be modified or cancelled anymore\n
                 * Cancelled: has been cancelled, can't be confirmed anymore"""),
        'shipping_carrier':fields.selection([('UPS','UPS'),('FEDEX','FEDEX'),
                                            ('USPS','USPS'),('STAMPS','STAMPS'),
                                            ('UPS WorldShip CSV','UPS WorldShip CSV'),('USPS DataPac CSV','USPS DataPac CSV'),
                                            ('Freight','Freight')],
                                            'Shipping Carrier'),
        # 'shipping_carrier':fields.selection(_get_shipping_carrier,'Shipping Carrier'),
        'shipping_service_freight' : fields.char('Shipping Freight Service'),
        'shipping_service_fedex':fields.selection([('FEDEX_GROUND', 'Ground Commercial'),
                                   ('FEDEX_2_DAY', '2Day'),
                                   ('FEDEX_2_DAY_AM', '2Day AM'),
                                   ('FEDEX_EXPRESS_SAVER', 'Express Saver'),
                                   ('STANDARD_OVERNIGHT', 'Standard Overnight'),
                                   ('FIRST_OVERNIGHT', 'First Overnight'),
                                   ('PRIORITY_OVERNIGHT', 'Priority Overnight'),
                                   ('INTERNATIONAL_ECONOMY', 'International Economy'),
                                   ('INTERNATIONAL_FIRST', 'International First'),
                                   ('INTERNATIONAL_PRIORITY', 'International Priority'), 
                                   ('GROUND_HOME_DELIVERY', 'Ground Home'),
                                   ('SMART_POST', 'Smart Post'),
                                   ], 'Shipping Service Fedex'),
        'shipping_service_ups':fields.selection([('GROUND', 'Ground'),
                                   ('UPSSTANDARDS','UPS Standards'),
                                   ('UPSSAVER','UPS Saver'),
                                   ('EXPRESS','Express'),
                                   ('EXPRESSPLUS','Express Plus'),
                                   ('EXPEDITED','Expedited'),
                                   ('NEXTDAYAIR', 'Next Day Air'),
                                   ('NEXTDAYAIRSAVER', 'Next Day Air Saver'),
                                   ('NEXTDAYAIREARLYAM', 'Next Day Air Early A.M.'),
                                   ('2NDDAYAIR', '2nd Day Air'),
                                   ('2NDDAYAIRAM', '2nd Day Air AM'),
                                   ('3DAYSELECT', '3 Day Select'),
                                   ('SUREPOST - SUREPOSTUNDER1LB','Sure Post - Sure Post Under 1Lb'),
                                   ('SUREPOST - SUREPOSTOVER1LB','Sure Post - Sure Post Over 1Lb'),
                                   ('MI - FIRST','MI - First'),
                                   ('MI - PRIORITYMAIL','MI - Priority Mail'),
                                   ('MI - EXPEDITEDMAILINNOVATIONS','MI - Expedited Mail Innovations'),
                                   ('MI - PRIORITYMAILINNOVATIONS','MI - Priority Mail Innovations'),
                                   ('MI - ECONOMYMAILINNOVATIONS','MI - Economy Mail Innovations'),
                                   ], 'Shipping Service UPS'),
        'shipping_service_usps':fields.selection([('FIRST', 'First'),
                                   ('PRIORITY', 'Priority'),
                                   ('EXPRESS', 'Express'),
                                   ('PARCELSELECT', 'Parcel Select'),
                                   ('LIBRARYMAIL ', 'Library Mail'),
                                   ('MEDIAMAIL', 'Media Mail'),
                                   ('CRITICALMAIL', 'Critical Mail'),
                                   ('FIRSTCLASSMAILINTERNATIONAL', 'First Class Mail - International'),
                                   ('FIRSTCLASSPACKAGEINTERNATIONAL', 'First Class Package - International'),
                                   ('PRIORITYMAILINTERNATIONAL', 'Priority Mail International'),
                                   ('EXPRESSMAILINTERNATIONAL', 'Express Mail International'),
                                   ], 'Shipping Service USPS'),
        'shipping_service_usps_stamps':fields.selection([('US-FC', 'USPS First-Class Mail'),
                                   ('US-PM', 'USPS Priority Mail'),
                                   ('US-XM', 'USPS Express Mail'),
                                   ('US-MM ', 'USPS Media Mail'),
                                   ('US-PP', 'USPS Parcel Post'),
                                   ('US-LM', 'USPS Library Mail'),
                                   ('US-EMI', 'USPS Express Mail International'),
                                   ('US-PMI', 'USPS Priority Mail International'),
                                   ('US-FCI', 'USPS First Class Mail International'),
                                   ('US-CM', 'USPS Critical Mail'),
                                   ('US-PS', 'USPS Parcel Select'),
                                   ], 'Shipping Service USPS STAMPS'),
        # 'shipping_service_lso':fields.selection([
                                           # ('1DAY_MORNING', 'Priority Basic'),
                                           # ], 'Shipping Service LSO'
                                          # ),
        # 'shipping_service_canada_post':fields.selection([
                                           # ('1DAY_MORNING', 'Priority Basic'),
                                           # ], 'Shipping Service Canada Post'
                                          # ),
        'shipping_packaging_fedex':fields.selection([('FEDEXENVELOPE', 'Fedex Envelope'),
                                      ('FEDEXBOX', 'Fedex Box'),
                                      ('FEDEXPAK', 'Fedex Pak'),
                                      ('FEDEXTUBE', 'Fedex Tube'),   
                                      ('FEDEX10KGBOX', 'Fedex 10 kg Box'),  
                                      ('FEDEX25KGBOX', 'Fedex 25 kg Box'),
                                      ], 'Shipping packaging Fedex'),
        'shipping_packaging_ups':fields.selection([('UPSLETTER', 'UPS Letter'),
                                     ('UPSEXPRESSBOX', 'UPS Express Box'),
                                     ('UPS25KGBOX', 'UPS 25 kg Box'),
                                     ('UPS10KGBOX', 'UPS 10 kg Box'),
                                     ('TUBE', 'UPS Tube'),
                                     ('PAK', 'UPS PAK'),
                                     ('PALLET', 'UPS Pallet'),
                                     ('SMALLEXPRESSBOX', 'UPS Small Box'),
                                     ('MEDIUMEXPRESSBOX', 'UPS Medium Box'),
                                     ('LARGEEXPRESSBOX', 'UPS Large Box'),
                                    ], 'Shipping Packaging UPS'),
        'shipping_packaging_usps':fields.selection([('CARD','USPS Card'),
                                      ('LETTER', 'Letter'),
                                      ('FLAT','Flat'),
                                      ('PARCEL', 'Parcel'),
                                      ('LARGEPARCEL', 'Large Parcel'),
                                      ('IRREGULARPARCEL', 'Irregular Parcel'),
                                      ('FLATRATEENVELOPE', 'Flat Rate Envelope'),
                                      ('FLATRATELEGALENVELOPE', 'Flat Rate Legal Envelope'),
                                      ('FLATRATEPADDEDENVELOPE', 'Flat Rate Padded Envelope'),
                                      ('FLATRATEGIFTCARDENVELOPE', 'Flat Rate Gift Card Envelope'),
                                      ('FLATRATEWINDOWENVELOPE', 'Flat Rate Window Envelope'),
                                      ('FLATRATECARDBOARDENVELOPE', 'Flat Rate Cardboard Envelope'),
                                      ('SMALLFLATRATEENVELOPE', 'Small Flat Rate Envelope'),
                                      ('SMALLFLATRATEBOX', 'Small Flat Rate Box'),
                                      ('MEDIUMFLATRATEBOX', 'Medium Flat Rate Box'),
                                      ('LARGEFLATRATEBOX', 'Large Flat Rate Box'), 
                                      ('REGIONALRATEBOXA', 'Regional Rate A'),
                                      ('REGIONALRATEBOXB', 'Regional Rate B'),
                                      ('REGIONALRATEBOXC', 'Regional Rate C'),
                                      ('LARGEFLATRATEBOARDGAMEBOX', 'Large Flat Rate Board Game Box'),
                                     ], 'Shipping Packaging USPS'), 
        'shipping_packaging_usps_stamps':fields.selection([('POSTCARD','Postcard'),
                                      ('LETTER', 'Letter'),
                                      ('LARGE ENVELOPE OR FLAT','Large Envelope or Flat'),
                                      ('THICK ENVELOPE', 'Thick Envelope'),
                                      ('PACKAGE', 'Package'),
                                      ('FLAT RATE BOX', 'Flat Rate Box'),
                                      ('SMALL FLAT RATE BOX', 'Small Flat Rate Box'),
                                      ('LARGE FLAT RATE BOX', 'Large Flat Rate Box'),
                                      ('FLAT RATE ENVELOPE', 'Flat Rate Envelope'),
                                      ('FLAT RATE PADDED ENVELOPE', 'Flat Rate Padded Envelope'),
                                      ('LARGE PACKAGE', 'Large Package'),
                                      ('OVERSIZED PACKAGE', 'Oversized Package'),
                                      ('REGIONAL RATE BOX A', 'Regional Rate Box A'),
                                      ('REGIONAL RATE BOX B', 'Regional Rate Box B'),
                                      ('LEGAL FLAT RATE ENVELOPE', 'Legal Flat Rate Envelope'),
                                      ('REGIONAL RATE BOX C', 'Regional Rate Box C'),
                                     ], 'Shipping Packaging USPS STAMPS'), 
        'exception_description':fields.text('EasyPost Exception',readonly=True),
        'total_weight': fields.function(_cal_total_weight, type='float', string='Total Weight',store=False),
        'two_days_old': fields.function(_cal_two_days_old, type='integer', string='2 Days Old',store=False),
        'seven_days_old': fields.function(_cal_seven_days_old, type='integer', string='7 Days Old',store=False),
        'insured':fields.boolean('Insure Shipping ?'),
        'value_insured':fields.float('Value Insured'),
        'refund_confirmation_number':fields.char('Refund Confirmation Number',size=100,readonly=True),
        'refund_status':fields.char('Refund Status',size=100,readonly=True),
        'refund_id':fields.char('Refund ID',size=100,readonly=True),
        'batch_no':fields.char('Batch No',size=100),
        'error_note':fields.text('Error Note'),
        'faulty':fields.boolean('Faulty'),
        'url_tracking_ref':fields.char('Web Tracking URL'),
        'label': fields.text('Url Label'),
        'list_tracking_numbers': fields.text('List of Tracking Numbers'),
        'ship_cal_id':fields.one2many('shipping.calculator','picking_id','Rate Line'),
    }

    #this because when a picking_in is fully processed, the order will still show "ready to process", let's correct it.
    def do_partial(self, cr, uid, ids, partial_datas, context=None):
        picked_order = super(stock_picking, self).do_partial(cr, uid, ids, partial_datas, context=context)
        pick_rec = self.browse(cr, uid, picked_order.keys()[0],context)
        for move in pick_rec.move_lines:
            if move.state!='done':
                #Only a partial delivery:
                return True
        #else if it's a full delivery
        return self.write(cr, uid, picked_order.keys()[0], {'state':'done'},context=context)

    def replaced_draft_force_assign(self, cr, uid, ids, context=None):
        """ Confirms picking directly from draft state.
        @return: True
        """
        move_obj = self.pool.get('stock.move')
        for pick in self.browse(cr, uid, ids):
            if not pick.move_lines:
                raise osv.except_osv(_('Error!'),_('You cannot process picking without stock moves.'))
            for move in pick.move_lines:
                move_obj.write(cr, uid, move.id,{'state': 'assigned'})
            self.write(cr, uid, pick.id,{'state': 'assigned'})
        return True


    def packaging_product(self, cr, uid, ids, context=None):
        move_obj = self.pool.get('stock.move')
        tracking_obj = self.pool.get('stock.tracking')
        pick_obj = self.browse(cr, uid, ids[0], context=None)
        for move_line in pick_obj.move_lines:
            
            if move_line.product_id.packaging and not move_line.tracking_id:
                p_qty = move_line.product_id.packaging[0].qty
                
                if p_qty < move_line.product_qty:
                    p_t_qty = move_line.product_qty/p_qty
                    print str(p_t_qty)
                    p_r_qty = move_line.product_qty%p_qty
                    print str(p_r_qty) 
                    tracking_id = tracking_obj.create(cr, uid, {'active':1,'box_setup_id':move_line.product_id.packaging[0].box_setup_id.id})
                    move_obj.write(cr, uid, move_line.id, {'product_uos_qty': p_qty,'product_qty': p_qty,'tracking_id': tracking_id})
                    
                    #Package products in full packages
                    for i in range(int(p_t_qty-1)):
                        move_id = move_obj.copy(cr, uid, move_line.id)
                        tracking_id = tracking_obj.create(cr, uid, {'active':1,'box_setup_id':move_line.product_id.packaging[0].box_setup_id.id})
                        move_obj.write(cr, uid, move_id, {'product_uos_qty': p_qty,'product_qty': p_qty, 'state': move_line.state, 'tracking_id' : tracking_id})
                    
                    #Package the rest of products which will not make a full box_setup
                    if p_r_qty > 0:  
                        move_id = move_obj.copy(cr, uid, move_line.id)
                        tracking_id = tracking_obj.create(cr, uid, {'active':1})
                        move_obj.write(cr, uid, move_id, {'product_uos_qty': p_r_qty,'product_qty': p_r_qty, 'state': move_line.state,'tracking_id' : tracking_id})
        
        return True
    

    def get_shipment_rate(self, cr, uid, ids, context=None): 

        obj = self.browse(cr,uid,ids[0],context=None)
        cr.execute(""" select easypost_api,usps,ups,fedex from res_company where id=%s""",(obj.company_id.id,))
        res_api = cr.fetchall()
        
        if not res_api[0][0]:
            raise osv.except_osv(_('Message!'),  _("Please enter Shippingapi key inside company!!!"))
         
        if res_api[0][1] is False and res_api[0][2] is False and res_api[0][3] is False:
            raise osv.except_osv(_('Message!'),  _("Please select shipping carrier inside company!!!"))
         
        
        cr.execute(""" delete from shipping_calculator where picking_id=%s""",(ids[0],))
        
        #This is a list of dictionaries which will contain all the values_lines of the shipping_calculator, it will be used to merge lines at the end of the loop
        list_of_dict_of_rates = {}
        
        for move in obj.move_lines:
            if not move.tracking_id:
                raise osv.except_osv(_('Message!'),  _("Please select Pack for every move line in your delivery order !"))
            
            #UNS Edited This,
            # if not res_weight:
            if move.override_weight==0.0 and not move.tracking_id.box_setup_id:
                raise osv.except_osv(_('Message!'),  _("Please choose a Box Setup inside Pack, or setup the weight and packing settings under product : %s configuration !" %(move.product_id.name,)))
            elif move.override_weight==0.0 and not move.tracking_id.box_setup_id.weight:
                raise osv.except_osv(_('Message!'),  _("Please enter weight inside Box Setup inside Pack, or setup the weight and packing settings under product configuration !"))
            
        
            easypost.api_key = res_api[0][0]
                  
            if not move.location_id.partner_id.name or not move.location_id.partner_id.street or not move.location_id.partner_id.city or not move.location_id.partner_id.state_id.name or not move.location_id.partner_id.zip:
                raise osv.except_osv(_('Source location address informations missing !'),  _("Please be sure to set the required data and informations about your source location address: name, address, city, state and zip code!"))
            fromAddress = easypost.Address.create(
                                                  company = move.location_id.partner_id.name,
                                                  street1 = move.location_id.partner_id.street,
                                                  street2 = move.location_id.partner_id.street2,
                                                  city =    move.location_id.partner_id.city,
                                                  state =   move.location_id.partner_id.state_id.name,
                                                  zip =     move.location_id.partner_id.zip,
                                                  phone =   move.location_id.partner_id.phone
                                                  )
            if not obj.partner_id.name or not obj.partner_id.street or not obj.partner_id.city or not obj.partner_id.state_id.name or not obj.partner_id.zip:
                raise osv.except_osv(_('Customer informations missing !'),  _("Please be sure to set the required data and informations about your customer: name, address, city, state and zip code!"))
            toAddress = easypost.Address.create(
                                                name = obj.partner_id.name or '',
                                                company = obj.company_id.name or '',
                                                street1 = obj.partner_id.street or '',
                                                city = obj.partner_id.city or '',
                                                state = obj.partner_id.state_id.name or '',
                                                zip = obj.partner_id.zip or ''
                                            )
            ################## shipping SmallFlatRateBox rate for usps #############
            parcelsmall = easypost.Parcel.create(
                                             weight = math.ceil(move.override_weight*16),
                                             predefined_package = 'SmallFlatRateBox',
                                            )
    
            shipmentsmallusps = easypost.Shipment.create(
                                                to_address = toAddress,
                                                from_address = fromAddress,
                                                parcel = parcelsmall
                                                )
                                                
            for rate in shipmentsmallusps.rates:
               
                if res_api[0][1] and res_api[0][2] and res_api[0][3]:
                    
                    if not list_of_dict_of_rates.has_key(str(rate.carrier)+str(rate.service)+'-SmallFlatRateBox'):
                        list_of_dict_of_rates[str(rate.carrier)+str(rate.service)+'-SmallFlatRateBox']={
                           'name' : rate.carrier,
                           'service' : rate.service,
                           'rate' : float(rate.rate),
                           'rate_description' : 'SmallFlatRateBox',
                           'picking_id' : ids[0],
                           'tracking_id' : move.tracking_id.id
                            }
                    else:
                        list_of_dict_of_rates[str(rate.carrier)+str(rate.service)+'-SmallFlatRateBox']['rate']+= float(rate.rate)
                    
                    #self.pool.get('shipping.calculator').create(cr,uid,val,context=None)
                elif res_api[0][1] and res_api[0][2]:
                    if not list_of_dict_of_rates.has_key(str(rate.carrier)+str(rate.service)+'-SmallFlatRateBox'):
                        list_of_dict_of_rates[str(rate.carrier)+str(rate.service)+'-SmallFlatRateBox']={
                           'name' : rate.carrier,
                           'service' : rate.service,
                           'rate' : float(rate.rate),
                           'rate_description' : 'SmallFlatRateBox',
                           'picking_id' : ids[0],
                           'tracking_id' : move.tracking_id.id
                               }
                    else:
                        list_of_dict_of_rates[str(rate.carrier)+str(rate.service)+'-SmallFlatRateBox']['rate']+= float(rate.rate)
                    #self.pool.get('shipping.calculator').create(cr,uid,val,context=None)
                elif res_api[0][1]:
                    
                    if rate.carrier == 'USPS':
                        if not list_of_dict_of_rates.has_key(str(rate.carrier)+str(rate.service)+'-SmallFlatRateBox'):
                            list_of_dict_of_rates[str(rate.carrier)+str(rate.service)+'-SmallFlatRateBox']={
                           'name' : rate.carrier,
                           'service' : rate.service,
                           'rate' : float(rate.rate),
                           'rate_description' : 'SmallFlatRateBox',
                           'picking_id' : ids[0],
                           'tracking_id' : move.tracking_id.id
                               }
                        else:
                            list_of_dict_of_rates[str(rate.carrier)+str(rate.service)+'-SmallFlatRateBox']['rate']+= float(rate.rate)
                        
                        #self.pool.get('shipping.calculator').create(cr,uid,val,context=None)
                        
                elif res_api[0][2]:
                    
                    if rate.carrier == 'UPS':
                        if not list_of_dict_of_rates.has_key(str(rate.carrier)+str(rate.service)+'-SmallFlatRateBox'):    
                            list_of_dict_of_rates[str(rate.carrier)+str(rate.service)+'-SmallFlatRateBox']={
                           'name' : rate.carrier,
                           'service' : rate.service,
                           'rate' : float(rate.rate),
                           'rate_description' : 'SmallFlatRateBox', 
                           'picking_id' : ids[0],
                           'tracking_id' : move.tracking_id.id
                               }
                        else:
                            list_of_dict_of_rates[str(rate.carrier)+str(rate.service)+'-SmallFlatRateBox']['rate']+= float(rate.rate)
                      
                       #self.pool.get('shipping.calculator').create(cr,uid,val,context=None)
                else:
                    print 'not availabe fedex price'

            ############################ shipping MediumFlatRateBox rate for usps ###############
            parcelmediam = easypost.Parcel.create(
                                             weight = math.ceil(move.override_weight*16),
                                             predefined_package = 'MediumFlatRateBox',
                                            )
            shipmentmediumusps = easypost.Shipment.create(
                                                to_address = toAddress,
                                                from_address = fromAddress,
                                                parcel = parcelmediam
                                                )
            
            for rate in shipmentmediumusps.rates:
                
                if res_api[0][1] and res_api[0][2] and res_api[0][3]:
                    if not list_of_dict_of_rates.has_key(str(rate.carrier)+str(rate.service)+'-MediumFlatRateBox'):
                        list_of_dict_of_rates[str(rate.carrier)+str(rate.service)+'-MediumFlatRateBox']={
                       'name' : rate.carrier,
                       'service' : rate.service,
                       'rate' : float(rate.rate),
                       'rate_description' : 'MediumFlatRateBox',
                       'picking_id' : ids[0],
                       'tracking_id' : move.tracking_id.id
                        }
                    else:
                        list_of_dict_of_rates[str(rate.carrier)+str(rate.service)+'-MediumFlatRateBox']['rate']+= float(rate.rate)
                    #self.pool.get('shipping.calculator').create(cr,uid,val,context=None)
                elif res_api[0][1] and res_api[0][2]:
                    if not list_of_dict_of_rates.has_key(str(rate.carrier)+str(rate.service)+'-MediumFlatRateBox'):
                        list_of_dict_of_rates[str(rate.carrier)+str(rate.service)+'-MediumFlatRateBox']={
                           'name' : rate.carrier,
                           'service' : rate.service,
                           'rate' : float(rate.rate),
                           'rate_description' : 'MediumFlatRateBox',
                           'picking_id' : ids[0],
                           'tracking_id' : move.tracking_id.id
                               }
                    else:
                        list_of_dict_of_rates[str(rate.carrier)+str(rate.service)+'-MediumFlatRateBox']['rate']+= float(rate.rate)
                    #self.pool.get('shipping.calculator').create(cr,uid,val,context=None)
                elif res_api[0][1]:
                    
                    if rate.carrier == 'USPS':
                        if not list_of_dict_of_rates.has_key(str(rate.carrier)+str(rate.service)+'-MediumFlatRateBox'):
                            list_of_dict_of_rates[str(rate.carrier)+str(rate.service)+'-MediumFlatRateBox']={
                           'name' : rate.carrier,
                           'service' : rate.service,
                           'rate' : float(rate.rate),
                           'rate_description' : 'MediumFlatRateBox',
                           'picking_id' : ids[0],
                           'tracking_id' : move.tracking_id.id
                               }
                        else:
                            list_of_dict_of_rates[str(rate.carrier)+str(rate.service)+'-MediumFlatRateBox']['rate']+= float(rate.rate)
                        #self.pool.get('shipping.calculator').create(cr,uid,val,context=None)
                        
                elif res_api[0][2]:
                    
                    if rate.carrier == 'UPS':
                        if not list_of_dict_of_rates.has_key(str(rate.carrier)+str(rate.service)+'-MediumFlatRateBox'):
                            list_of_dict_of_rates[str(rate.carrier)+str(rate.service)+'-MediumFlatRateBox']={
                           'name' : rate.carrier,
                           'service' : rate.service,
                           'rate' : float(rate.rate),
                           'rate_description' : 'MediumFlatRateBox',
                           'picking_id' : ids[0],
                           'tracking_id' : move.tracking_id.id
                               }
                        else:
                            list_of_dict_of_rates[str(rate.carrier)+str(rate.service)+'-MediumFlatRateBox']['rate']+= float(rate.rate)
                       #self.pool.get('shipping.calculator').create(cr,uid,val,context=None)
                else: 
                    print 'not availabe fedex price'

            ############################ shipping RegionalRateBoxA rate for usps ###############
            
            parcelregionala = easypost.Parcel.create(
                                             weight = math.ceil(move.override_weight*16),
                                             predefined_package = 'RegionalRateBoxA',
                                            )
            shipmentregionala = easypost.Shipment.create(
                                                to_address = toAddress,
                                                from_address = fromAddress,
                                                parcel = parcelregionala
                                                )
                                                
            for rate in shipmentregionala.rates:
                
                if res_api[0][1] and res_api[0][2] and res_api[0][3]:
                    if not list_of_dict_of_rates.has_key(str(rate.carrier)+str(rate.service)+'-RegionalRateBoxA'):
                        list_of_dict_of_rates[str(rate.carrier)+str(rate.service)+'-RegionalRateBoxA']={
                       'name' : rate.carrier,
                       'service' : rate.service,
                       'rate' : float(rate.rate),
                       'rate_description' : 'RegionalRateBoxA',
                       'picking_id' : ids[0],
                       'tracking_id' : move.tracking_id.id
                        }
                    else:
                        list_of_dict_of_rates[str(rate.carrier)+str(rate.service)+'-RegionalRateBoxA']['rate']+= float(rate.rate)
                    #self.pool.get('shipping.calculator').create(cr,uid,val,context=None)
                elif res_api[0][1] and res_api[0][2]:
                    if not list_of_dict_of_rates.has_key(str(rate.carrier)+str(rate.service)+'-RegionalRateBoxA'):
                        list_of_dict_of_rates[str(rate.carrier)+str(rate.service)+'-RegionalRateBoxA']={
                           'name' : rate.carrier,
                           'service' : rate.service,
                           'rate' : float(rate.rate),
                           'rate_description' : 'RegionalRateBoxA',
                           'picking_id' : ids[0],
                           'tracking_id' : move.tracking_id.id
                               }
                    else:
                        list_of_dict_of_rates[str(rate.carrier)+str(rate.service)+'-RegionalRateBoxA']['rate']+= float(rate.rate)
                    #self.pool.get('shipping.calculator').create(cr,uid,val,context=None)
                elif res_api[0][1]:
                    
                    if rate.carrier == 'USPS':
                        if not list_of_dict_of_rates.has_key(str(rate.carrier)+str(rate.service)+'-RegionalRateBoxA'):
                            list_of_dict_of_rates[str(rate.carrier)+str(rate.service)+'-RegionalRateBoxA']={
                           'name' : rate.carrier,
                           'service' : rate.service,
                           'rate' : float(rate.rate),
                           'rate_description' : 'RegionalRateBoxA',
                           'picking_id' : ids[0],
                           'tracking_id' : move.tracking_id.id
                               }
                        else:
                            list_of_dict_of_rates[str(rate.carrier)+str(rate.service)+'-RegionalRateBoxA']['rate']+= float(rate.rate)
                        #self.pool.get('shipping.calculator').create(cr,uid,val,context=None)
                        
                elif res_api[0][2]:
                    
                    if rate.carrier == 'UPS':
                        if not list_of_dict_of_rates.has_key(str(rate.carrier)+str(rate.service)+'-RegionalRateBoxA'):
                            list_of_dict_of_rates[str(rate.carrier)+str(rate.service)+'-RegionalRateBoxA']={
                           'name' : rate.carrier,
                           'service' : rate.service,
                           'rate' : float(rate.rate),
                           'rate_description' : 'RegionalRateBoxA',
                           'picking_id' : ids[0],
                           'tracking_id' : move.tracking_id.id
                               }
                        else:
                            list_of_dict_of_rates[str(rate.carrier)+str(rate.service)+'-RegionalRateBoxA']['rate']+= float(rate.rate)
                       #self.pool.get('shipping.calculator').create(cr,uid,val,context=None)
                else:
                    print 'not availabe fedex price'

            ############################ shipping RegionalRateBoxB rate for usps ###############
            
            parcelregionalb = easypost.Parcel.create(
                                             weight = math.ceil(move.override_weight*16),
                                             predefined_package = 'RegionalRateBoxB',
                                            )
            shipmentregionalb = easypost.Shipment.create(
                                                to_address = toAddress,
                                                from_address = fromAddress,
                                                parcel = parcelregionalb
                                                )
                                                
            for rate in shipmentregionalb.rates:
                
                if res_api[0][1] and res_api[0][2] and res_api[0][3]:
                    if not list_of_dict_of_rates.has_key(str(rate.carrier)+str(rate.service)+'-RegionalRateBoxB'):
                        list_of_dict_of_rates[str(rate.carrier)+str(rate.service)+'-RegionalRateBoxB']={
                       'name' : rate.carrier,
                       'service' : rate.service,
                       'rate' : float(rate.rate),
                       'rate_description' : 'RegionalRateBoxB',
                       'picking_id' : ids[0],
                       'tracking_id' : move.tracking_id.id
                        }
                    else:
                        list_of_dict_of_rates[str(rate.carrier)+str(rate.service)+'-RegionalRateBoxB']['rate']+= float(rate.rate)
                    #self.pool.get('shipping.calculator').create(cr,uid,val,context=None)
                elif res_api[0][1] and res_api[0][2]:
                    if not list_of_dict_of_rates.has_key(str(rate.carrier)+str(rate.service)+'-RegionalRateBoxB'):
                        list_of_dict_of_rates[str(rate.carrier)+str(rate.service)+'-RegionalRateBoxB']={
                           'name' : rate.carrier,
                           'service' : rate.service,
                           'rate' : float(rate.rate),
                           'rate_description' : 'RegionalRateBoxB',
                           'picking_id' : ids[0],
                           'tracking_id' : move.tracking_id.id
                               }
                    else:
                        list_of_dict_of_rates[str(rate.carrier)+str(rate.service)+'-RegionalRateBoxB']['rate']+= float(rate.rate)
                    #self.pool.get('shipping.calculator').create(cr,uid,val,context=None)
                elif res_api[0][1]:
                    
                    if rate.carrier == 'USPS':
                        if not list_of_dict_of_rates.has_key(str(rate.carrier)+str(rate.service)+'-RegionalRateBoxB'):
                            list_of_dict_of_rates[str(rate.carrier)+str(rate.service)+'-RegionalRateBoxB']={
                           'name' : rate.carrier,
                           'service' : rate.service,
                           'rate' : float(rate.rate),
                           'rate_description' : 'RegionalRateBoxB',
                           'picking_id' : ids[0],
                           'tracking_id' : move.tracking_id.id
                               }
                        else:
                            list_of_dict_of_rates[str(rate.carrier)+str(rate.service)+'-RegionalRateBoxB']['rate']+= float(rate.rate)
                        #self.pool.get('shipping.calculator').create(cr,uid,val,context=None)
                        
                elif res_api[0][2]:
                    
                    if rate.carrier == 'UPS':
                        if not list_of_dict_of_rates.has_key(str(rate.carrier)+str(rate.service)+'-RegionalRateBoxB'):
                            list_of_dict_of_rates[str(rate.carrier)+str(rate.service)+'-RegionalRateBoxB']={
                           'name' : rate.carrier,
                           'service' : rate.service,
                           'rate' : float(rate.rate),
                           'rate_description' : 'RegionalRateBoxB',
                           'picking_id' : ids[0],
                           'tracking_id' : move.tracking_id.id
                               }
                        else:
                            list_of_dict_of_rates[str(rate.carrier)+str(rate.service)+'-RegionalRateBoxB']['rate']+= float(rate.rate)
                       #self.pool.get('shipping.calculator').create(cr,uid,val,context=None)
                else:
                    print 'not availabe fedex price'   

            ############################ shipping RegionalRateBoxC rate for usps ###############
            
            parcelregionalc = easypost.Parcel.create(
                                             weight = math.ceil(move.override_weight*16),
                                             predefined_package = 'RegionalRateBoxC',
                                            )
            shipmentregionalc = easypost.Shipment.create(
                                                to_address = toAddress,
                                                from_address = fromAddress,
                                                parcel = parcelregionalc
                                                )
                                                
            for rate in shipmentregionalc.rates:
                
                if res_api[0][1] and res_api[0][2] and res_api[0][3]:
                    if not list_of_dict_of_rates.has_key(str(rate.carrier)+str(rate.service)+'-RegionalRateBoxC'):
                        list_of_dict_of_rates[str(rate.carrier)+str(rate.service)+'-RegionalRateBoxC']={
                       'name' : rate.carrier,
                       'service' : rate.service,
                       'rate' : float(rate.rate),
                       'rate_description' : 'RegionalRateBoxC',
                       'picking_id' : ids[0],
                       'tracking_id' : move.tracking_id.id
                        }
                    else:
                        list_of_dict_of_rates[str(rate.carrier)+str(rate.service)+'-RegionalRateBoxC']['rate']+= float(rate.rate)
                    #self.pool.get('shipping.calculator').create(cr,uid,val,context=None)
                elif res_api[0][1] and res_api[0][2]:
                    if not list_of_dict_of_rates.has_key(str(rate.carrier)+str(rate.service)+'-RegionalRateBoxC'):
                        list_of_dict_of_rates[str(rate.carrier)+str(rate.service)+'-RegionalRateBoxC']={
                           'name' : rate.carrier,
                           'service' : rate.service,
                           'rate' : float(rate.rate),
                           'rate_description' : 'RegionalRateBoxC',
                           'picking_id' : ids[0],
                           'tracking_id' : move.tracking_id.id
                               }
                    else:
                        list_of_dict_of_rates[str(rate.carrier)+str(rate.service)+'-RegionalRateBoxC']['rate']+= float(rate.rate)
                    #self.pool.get('shipping.calculator').create(cr,uid,val,context=None)
                elif res_api[0][1]:
                    
                    if rate.carrier == 'USPS':
                        if not list_of_dict_of_rates.has_key(str(rate.carrier)+str(rate.service)+'-RegionalRateBoxC'):
                            list_of_dict_of_rates[str(rate.carrier)+str(rate.service)+'-RegionalRateBoxC']={
                           'name' : rate.carrier,
                           'service' : rate.service,
                           'rate' : float(rate.rate),
                           'rate_description' : 'RegionalRateBoxC',
                           'picking_id' : ids[0],
                           'tracking_id' : move.tracking_id.id
                               }
                        else:
                            list_of_dict_of_rates[str(rate.carrier)+str(rate.service)+'-RegionalRateBoxC']['rate']+= float(rate.rate)
                        #self.pool.get('shipping.calculator').create(cr,uid,val,context=None)
                        
                elif res_api[0][2]:
                    
                    if rate.carrier == 'UPS':
                        if not list_of_dict_of_rates.has_key(str(rate.carrier)+str(rate.service)+'-RegionalRateBoxC'):
                            list_of_dict_of_rates[str(rate.carrier)+str(rate.service)+'-RegionalRateBoxC']={
                           'name' : rate.carrier,
                           'service' : rate.service,
                           'rate' : float(rate.rate),
                           'rate_description' : 'RegionalRateBoxC',
                           'picking_id' : ids[0],
                           'tracking_id' : move.tracking_id.id
                               }
                        else:
                            list_of_dict_of_rates[str(rate.carrier)+str(rate.service)+'-RegionalRateBoxC']['rate']+= float(rate.rate)
                       #self.pool.get('shipping.calculator').create(cr,uid,val,context=None)
                else:
                    print 'not availabe fedex price'               

            ####################### SmallExpressBox rate for ups ####################       
            
            parcelsmallups = easypost.Parcel.create(
                                             weight = math.ceil(move.override_weight*16),
                                             predefined_package = 'SmallExpressBox',
                                            )
            shipmentsmallups = easypost.Shipment.create(
                                                to_address = toAddress,
                                                from_address = fromAddress,
                                                parcel = parcelsmallups
                                                )
            for rate in shipmentsmallups.rates:
    
    
                if res_api[0][1] and res_api[0][2] and res_api[0][3]:
                    if not list_of_dict_of_rates.has_key(str(rate.carrier)+str(rate.service)+'-SmallExpressBox'):
                        list_of_dict_of_rates[str(rate.carrier)+str(rate.service)+'-SmallExpressBox']={
                       'name' : rate.carrier,
                       'service' : rate.service,
                       'rate' : float(rate.rate),
                       'rate_description' : 'SmallExpressBox',
                       'picking_id' : ids[0],
                       'tracking_id' : move.tracking_id.id
                        }
                    else:
                        list_of_dict_of_rates[str(rate.carrier)+str(rate.service)+'-SmallExpressBox']['rate']+= float(rate.rate)
                    #self.pool.get('shipping.calculator').create(cr,uid,val,context=None)
                elif res_api[0][1] and res_api[0][2]:
                    if not list_of_dict_of_rates.has_key(str(rate.carrier)+str(rate.service)+'-SmallExpressBox'):
                        list_of_dict_of_rates[str(rate.carrier)+str(rate.service)+'-SmallExpressBox']={
                           'name' : rate.carrier,
                           'service' : rate.service,
                           'rate' : float(rate.rate),
                           'rate_description' : 'SmallExpressBox', 
                           'picking_id' : ids[0],
                           'tracking_id' : move.tracking_id.id
                               }
                    else:
                        list_of_dict_of_rates[str(rate.carrier)+str(rate.service)+'-SmallExpressBox']['rate']+= float(rate.rate)
                    #self.pool.get('shipping.calculator').create(cr,uid,val,context=None)
                elif res_api[0][1]:
                    
                    if rate.carrier == 'USPS':
                        if not list_of_dict_of_rates.has_key(str(rate.carrier)+str(rate.service)+'-SmallExpressBox'):
                            list_of_dict_of_rates[str(rate.carrier)+str(rate.service)+'-SmallExpressBox']={
                           'name' : rate.carrier,
                           'service' : rate.service,
                           'rate' : float(rate.rate),
                           'rate_description' : 'SmallExpressBox', 
                           'picking_id' : ids[0],
                           'tracking_id' : move.tracking_id.id
                               }
                        else:
                            list_of_dict_of_rates[str(rate.carrier)+str(rate.service)+'-SmallExpressBox']['rate']+= float(rate.rate)
                        #self.pool.get('shipping.calculator').create(cr,uid,val,context=None)
                        
                elif res_api[0][2]:
                    
                    if rate.carrier == 'UPS':
                        if not list_of_dict_of_rates.has_key(str(rate.carrier)+str(rate.service)+'-SmallExpressBox'):
                            list_of_dict_of_rates[str(rate.carrier)+str(rate.service)+'-SmallExpressBox']={
                           'name' : rate.carrier,
                           'service' : rate.service,
                           'rate' : float(rate.rate),
                           'rate_description' : 'SmallExpressBox', 
                           'picking_id' : ids[0],
                           'tracking_id' : move.tracking_id.id
                               }
                        else:
                            list_of_dict_of_rates[str(rate.carrier)+str(rate.service)+'-SmallExpressBox']['rate']+= float(rate.rate)
                       #self.pool.get('shipping.calculator').create(cr,uid,val,context=None)
                else:
                    
                    print 'not availabe fedex price'
            ############### MediumExpressBox rate for ups ##################           
            parcelmediamups = easypost.Parcel.create(
                                            weight = math.ceil(move.override_weight*16),
                                            predefined_package = 'MediumExpressBox',
                                            )
    
            shipmentmediamups = easypost.Shipment.create(
                                                to_address = toAddress,
                                                from_address = fromAddress,
                                                parcel = parcelmediamups
                                                )
            for rate in shipmentmediamups.rates:
                
                if res_api[0][1] and res_api[0][2] and res_api[0][3]:
                    if not list_of_dict_of_rates.has_key(str(rate.carrier)+str(rate.service)+'-MediumExpressBox'):
                        list_of_dict_of_rates[str(rate.carrier)+str(rate.service)+'-MediumExpressBox']={
                       'name' : rate.carrier,
                       'service' : rate.service,
                       'rate' : float(rate.rate),
                       'rate_description' : 'MediumExpressBox',  
                       'picking_id' : ids[0],
                       'tracking_id' : move.tracking_id.id
                        }
                    else:
                        list_of_dict_of_rates[str(rate.carrier)+str(rate.service)+'-MediumExpressBox']['rate']+= float(rate.rate)
                    #self.pool.get('shipping.calculator').create(cr,uid,val,context=None)
                elif res_api[0][1] and res_api[0][2]:
                    if not list_of_dict_of_rates.has_key(str(rate.carrier)+str(rate.service)+'-MediumExpressBox'):
                        list_of_dict_of_rates[str(rate.carrier)+str(rate.service)+'-MediumExpressBox']={
                           'name' : rate.carrier,
                           'service' : rate.service,
                           'rate' : float(rate.rate),
                           'rate_description' : 'MediumExpressBox',  
                           'picking_id' : ids[0],
                           'tracking_id' : move.tracking_id.id
                               }
                    else:
                        list_of_dict_of_rates[str(rate.carrier)+str(rate.service)+'-MediumExpressBox']['rate']+= float(rate.rate)
                    #self.pool.get('shipping.calculator').create(cr,uid,val,context=None)
                elif res_api[0][1]:
                    
                    if rate.carrier == 'USPS':
                        if not list_of_dict_of_rates.has_key(str(rate.carrier)+str(rate.service)+'-MediumExpressBox'):
                            list_of_dict_of_rates[str(rate.carrier)+str(rate.service)+'-MediumExpressBox']={
                           'name' : rate.carrier,
                           'service' : rate.service,
                           'rate' : float(rate.rate),
                           'rate_description' : 'MediumExpressBox',   
                           'picking_id' : ids[0],
                           'tracking_id' : move.tracking_id.id
                               }
                        else:
                            list_of_dict_of_rates[str(rate.carrier)+str(rate.service)+'-MediumExpressBox']['rate']+= float(rate.rate)
                        #self.pool.get('shipping.calculator').create(cr,uid,val,context=None)
                        
                elif res_api[0][2]:
                    
                    if rate.carrier == 'UPS':
                        if not list_of_dict_of_rates.has_key(str(rate.carrier)+str(rate.service)+'-MediumExpressBox'):
                            list_of_dict_of_rates[str(rate.carrier)+str(rate.service)+'-MediumExpressBox']={
                           'name' : rate.carrier,
                           'service' : rate.service,
                           'rate' : float(rate.rate),
                           'rate_description' : 'MediumExpressBox',   
                           'picking_id' : ids[0],
                           'tracking_id' : move.tracking_id.id
                               }
                        else:
                            list_of_dict_of_rates[str(rate.carrier)+str(rate.service)+'-MediumExpressBox']['rate']+= float(rate.rate)
                       #self.pool.get('shipping.calculator').create(cr,uid,val,context=None)
                else:
                    
                    print 'not availabe fedex price'
            ############### GENERAL withour specifing package (to extract GROUND specially) rate for ups ##################           
            
            try:
                if move.tracking_id.box_setup_id:
                    if move.tracking_id.box_setup_id.length and move.tracking_id.box_setup_id.width and move.tracking_id.box_setup_id.height:
                        parcelmediamups = easypost.Parcel.create(
                                  length = move.tracking_id.box_setup_id.length,
                                  width  = move.tracking_id.box_setup_id.width,
                                  height = move.tracking_id.box_setup_id.height,
                                  weight = math.ceil(move.override_weight*16)
                        )

                        shipmentmediamups = easypost.Shipment.create(
                                                            to_address = toAddress,
                                                            from_address = fromAddress,
                                                            parcel = parcelmediamups
                                                            )
                        # assert False, shipmentmediamups
                        for rate in shipmentmediamups.rates:
                            if (rate.carrier=="UPS" and rate.service=="Ground") or (rate.carrier=="USPS" and rate.service=="Priority"):
                                if not list_of_dict_of_rates.has_key(str(rate.carrier)+str(rate.service)+'-PARCEL'):
                                    list_of_dict_of_rates[str(rate.carrier)+str(rate.service)+'-PARCEL']={
                                   'name' : rate.carrier,
                                   'service' : rate.service,
                                   'rate' : float(rate.rate),
                                   'rate_description' : 'PARCEL',  
                                   'picking_id' : ids[0],
                                   'tracking_id' : move.tracking_id.id
                                    }
                                else:
                                    list_of_dict_of_rates[str(rate.carrier)+str(rate.service)+'-PARCEL']['rate']+= float(rate.rate)
                                #self.pool.get('shipping.calculator').create(cr,uid,val,context=None)
            except easypost.Error as e:
                    raise osv.except_osv(_('Error !'),  _(e))
        #Now After looping on all moves of the delivery (packages), and since we arranged in the dictionary 'list_of_dict_of_rates' all dictionaries 
        #of values to insert into the shipping_calculator table, lets loop on this dictionary "list_of_dict_of_rates" and insert values.
        for key, value in list_of_dict_of_rates.items():
            self.pool.get('shipping.calculator').create(cr, uid, value, context=None)
        return True


    def add_to_ship_que(self, cr, uid, ids, context=None):

        for id in ids:
            # pick_obj = self.browse(cr, uid, id)
            #line_obj = self.pool.get('stock.move')
            # warehouse_id = pick_obj.sale_id.shop_id.warehouse_id.id 
            #warehouse_id = 1
            
            #MAKE MOVE LINES, AND MAKE DELIVERY ORDER as Done
            delivery_actioned = self.action_move(cr, uid, [id])
            # delivery_actioned = self.action_done(cr, uid, [id])
            self.write(cr, uid, id, {'state': 'ship_queue'})
            # UNS Removed This.
            # cr.execute('select distinct location_id from stock_move where picking_id=%s',(id,))
            # result= cr.fetchall()
            # i = 0
            # for rs in result:
                # if i<>0:
                    # order_id = self.copy(cr, uid, id)
                    # cr.execute("update stock_picking set state='ship_queue',origin=%s where id=%s",(pick_obj.origin,order_id,))
                    # cr.execute('delete from stock_move where picking_id=%s',(order_id,))
                # #    for line in pick_obj.move_lines:
                # #        if line.location_id.id == rs[0]:
                # #            j = line.id
                # #            line_obj.write(cr, uid, j, {'picking_id': order_id })
                    # cr.execute('update stock_move set picking_id=%s where picking_id=%s and location_id=%s',(order_id,id,rs[0],))
                # i+= 1
        return True

    def remove_from_ship_que(self, cr, uid, ids, context=None):
        for id in ids:
            self.action_revert_done(cr, uid, [id], context)
        return True


    def split_order_wr(self, cr, uid, ids, context=None):
        for id in ids:     
            
            cr.execute('select distinct location_id from stock_move where picking_id=%s',(id,))
            result= cr.fetchall()
            i = 0
            for rs in result:
                if i<>0:
                    order_id = self.copy(cr, uid, id)
                    origin = self.browse(cr, uid, id).origin
                    state = self.browse(cr, uid, id).state
                    self.write(cr, uid, order_id,{'state': state, 'origin': origin,'move_lines': [(6, 0, [])] })
                    cr.execute('update stock_move set picking_id=%s where picking_id=%s and location_id=%s',(order_id,id,rs[0],))
                i+= 1
            

        return True 


    def cancel_shipment(self, cr, uid, ids, context=None):
        picking_out_record = self.browse(cr,uid,ids[0],context=None)
        move_obj = self.pool.get('stock.move')
        res_users_object = self.pool.get('res.users')
        user_record = res_users_object.browse(cr,uid,uid,context=None)
        if picking_out_record.shipping_carrier in ('UPS','USPS','FEDEX'):
            if picking_out_record.carrier_tracking_ref:
                if not picking_out_record.company_id.easypost_api:
                    raise osv.except_osv(_('Message!'),  _("Please enter easypost Shipping API key inside company !"))
                easypost.api_key = picking_out_record.company_id.easypost_api
                for move in picking_out_record.move_lines:
                    if move.carrier_tracking_ref:
                        move_refund = easypost.Refund.create(
                            carrier = picking_out_record.shipping_carrier,
                            tracking_codes = move.carrier_tracking_ref,
                        )
                        move_obj.write(cr, uid, move.id, {'state': 'cancel', 'label_state': 'cancel', 'refund_confirmation_number': move_refund[0].confirmation_number, 'refund_status': move_refund[0].status, 'refund_id':move_refund[0].id})
                        self.write(cr, uid, ids[0], {'state': 'cancel'})
                    else:
                        move_obj.write(cr, uid, move.id, {'state': 'cancel', 'label_state': 'cancel',})
                        self.write(cr, uid, ids[0], {'state': 'cancel'})
            else:
                for move in picking_out_record.move_lines:
                    move_obj.write(cr, uid, move.id, {'state': 'cancel', 'label_state': 'cancel',})
                self.write(cr, uid, ids[0], {'state': 'cancel', 'label_state': 'cancel',}) 
        # FIX ME : This is an on hold task, which should fix stamps refund button.
        elif picking_out_record.shipping_carrier=='STAMPS':
            if picking_out_record.carrier_tracking_ref:
                if ( not picking_out_record.company_id.stamps_integ_id or not picking_out_record.company_id.stamps_username or not picking_out_record.company_id.stamps_password):
                    raise osv.except_osv(_('Message!'),  _("Please enter Integration Details for Stamps inside company !"))
                integration_id = picking_out_record.company_id.stamps_integ_id
                username = picking_out_record.company_id.stamps_username
                password = picking_out_record.company_id.stamps_password
                if picking_out_record.company_id.stamps_prod_vs_staging=="Production":
                    stamps_configuration = StampsConfiguration(integration_id=integration_id,username=username, password=password)
                else:
                    stamps_configuration = StampsConfiguration(wsdl="testing",integration_id=integration_id,username=username, password=password)
                stamps_service = StampsService(configuration=stamps_configuration)
                for move in picking_out_record.move_lines:
                    if move.carrier_tracking_ref:
                        move_refund = stamps_service.remove_label_customised(picking_out_record.carrier_tracking_ref)
                        move_obj.write(cr, uid, move.id, {'state': 'cancel', 'label_state': 'cancel', 'refund_confirmation_number': move_refund[0].confirmation_number, 'refund_status': move_refund[0].status, 'refund_id':move_refund[0].id})
                        self.write(cr, uid, ids[0], {'state': 'cancel'})
                    else:
                        move_obj.write(cr, uid, move.id, {'state': 'cancel', 'label_state': 'cancel',})
                        self.write(cr, uid, ids[0], {'state': 'cancel'}) 
            else:
                for move in picking_out_record.move_lines:
                    move_obj.write(cr, uid, move.id, {'state': 'cancel', 'label_state': 'cancel',})
                self.write(cr, uid, ids[0], {'state': 'cancel'}) 
        else:
            for move in picking_out_record.move_lines:
                move_obj.write(cr, uid, move.id, {'state': 'cancel', 'label_state': 'cancel',})
            self.write(cr, uid, ids[0], {'state': 'cancel', 'label_state': 'cancel',}) 
        return True


    def send_to_warehouse(self,cr,uid,ids,context=None):
        
        
        if ids:
             
            for pick_id in ids:
                 
                cr.execute(""" select warehouse_id from stock_picking where id=%s""",(pick_id,))
                ware_id = cr.fetchall()
                 
                cr.execute(""" select  ftp,server_add,username,pass,folder_name from stock_warehouse where id=%s""",(ware_id[0][0],))
                res_ftp = cr.fetchall()
                 
                cr.execute(""" select name,db_datas from ir_attachment where res_id=%s""",(pick_id,))
                result_attach_id = cr.fetchall()
                 
                print 'result_attach_id print'
                print result_attach_id
                 
                if not res_ftp[0][0] == None and res_ftp[0][1] and res_ftp[0][2] and res_ftp[0][3]:
        
                    for attach_id in result_attach_id:
        
                        abc = attach_id[0]
            
                        ab = abc[53:]
                        import base64
                        imgdata = base64.b64decode(attach_id[1])
            
                        filename =  res_ftp[0][4] + ab
                        with open(filename, 'wb') as f:
                            f.write(imgdata)
                        
                        import os
                        import paramiko 
                        ssh = paramiko.SSHClient()
            
                        ssh.set_missing_host_key_policy(paramiko.AutoAddPolicy())
            
            
                        ssh.connect(res_ftp[0][1], username=res_ftp[0][2], password=res_ftp[0][3])
                        sftp = ssh.open_sftp()
            
            
                        localpath =  res_ftp[0][4] + ab
                    #   remotepath = '/home/user2/back up crish/' + ab
       
                    #   sftp.put(localpath, remotepath)
                    #   sftp.close()
                        ssh.close()
            
            

                 
                cr.execute(""" select  email_check from stock_warehouse where id=%s""",(ware_id[0][0],))
                emailcheck_res = cr.fetchall()
                 
                cr.execute(""" select  email from stock_warehouse where id=%s""",(ware_id[0][0],))
                email_res = cr.fetchall()
                 
                if not emailcheck_res[0][0] == None:
                     
                    if not email_res[0][0] == None:
                         
                        cr.execute(""" select id from ir_attachment where res_id=%s""",(pick_id,))
                        attach_id = cr.fetchall()
                        template_id = self.pool.get('ir.model.data').get_object_reference(cr, uid, 'shipping_postmaster', 'email_label_send_to_warehouse')[1]
                        cr.execute(""" delete from email_template_attachment_rel where email_template_id=%s""",(template_id,))
                        for attch in attach_id:
                         
                            cr.execute("insert into email_template_attachment_rel(email_template_id,attachment_id)values(%s,%s)",(template_id,attch[0],))
 
                       
                        self.pool.get('email.template').write(cr, uid, template_id, {'email_to':email_res[0][0]}, context=context)
                         
                        self.pool.get('email.template').send_mail(cr, uid, template_id, pick_id,True, context=context)
                         
                    else:
                        raise osv.except_osv(_('Message!'),  _("Please add email id in Warehouse Master"))
                else:
                    print 'not send'
         

                return True   

    #THIS WAS FOR TEST PURPOSES, don't delete it, its a demo function.
    # def get_stamps_rate(self, service):
        # """Get a test rate.

        # :param service: Instance of the stamps service.
        # """
        # ret_val = service.create_shipping()
        # ret_val.ShipDate = date.today().isoformat()
        # ret_val.FromZIPCode = "94107"
        # ret_val.ToZIPCode = "20500"
        # ret_val.PackageType = "Letter"
        # rate = service.get_rates(ret_val)[0]
        # ret_val.Amount = rate.Amount
        # ret_val.ServiceType = rate.ServiceType
        # ret_val.DeliverDays = rate.DeliverDays
        # ret_val.DimWeighting = rate.DimWeighting
        # ret_val.Zone = rate.Zone
        # ret_val.RateCategory = rate.RateCategory
        # ret_val.ToState = rate.ToState
        # add_on = service.create_add_on()
        # add_on.AddOnType = "US-A-DC"
        # ret_val.AddOns.AddOnV6.append(add_on)

        # return ret_val
        
    def get_stamps_address(self, service, FullName, Address1, Address2, City, State, ZIPCode, Country, Phone):
        """Get a test 'from' address.

        :param service: Instance of the stamps service.
        """
        address = service.create_address()
        address.FullName = FullName
        address.Address1 = Address1
        address.Address2 = Address2
        address.City = City
        address.State = State
        address.ZIPCode = ZIPCode
        string_Phone = str(Phone)
        for char in string_Phone:
            if char in " -?.!/;:":
                string_Phone.replace(char,'')
        address.PhoneNumber = string_Phone
        address.Country = Country   
        return service.get_address(address).Address

    def create_customsline(self, service, Description,Quantity, Value,WeightLb,HSTariffNumber,CountryOfOrigin):
        customs_line = service.create_customsline_wsdl()
        customs_line.Description = Description
        customs_line.Quantity = Quantity
        customs_line.Value = Value
        customs_line.WeightLb = WeightLb
        customs_line.HSTariffNumber = HSTariffNumber
        customs_line.CountryOfOrigin = CountryOfOrigin
        return customs_line
        
    def create_customs(self, service, ContentType, OtherDescribe,CustomsLines):
        customs = service.create_customs_wsdl()
        ArrayOfCustomsLine = service.create_ArrayOfCustomsLine_wsdl()
        customs.ContentType = ContentType
        
        if ContentType!="Other":
            customs.OtherDescribe = OtherDescribe
        ArrayOfCustomsLine.CustomsLine = CustomsLines
        customs.CustomsLines = ArrayOfCustomsLine
        return customs
        
    def update_label_status(self, cr, uid, ids, context=None):
    
        # #Picking Corrector : CHange Dlivery orders with prefix IN and INT into OUT.
        # picking_obj = self.pool.get('stock.picking.out')
        # faulty = picking_obj.search(cr, uid, [('name','like','IN'),('type','=','out')])
        # for record in picking_obj.browse(cr, uid, faulty,context):
        #     old_name = record.name
        #     name = 'OUT'+old_name.strip('INT').strip('IN')
        #     picking_obj.write(cr, uid, record.id, {'name':name})
        # return True

        # #Picking Corrector : Correct Move lines locations, to be defaulted from Warehouse or Company configured under delivery.
        # picking_obj = self.pool.get('stock.picking.out')
        # move_obj = self.pool.get('stock.move')
        # warehouse_obj = self.pool.get('stock.warehouse')
        # picking_out_in_ids = picking_obj.search(cr, uid, [('type','!=','internal')])

        # #UNS CUSTOMISATIONS START
        # for picking in picking_obj.browse(cr, uid, picking_out_in_ids,context):
        #     try:
        #         if picking.warehouse_id:
        #             picking_warehouse_id = picking.warehouse_id.id
        #             if picking.type=="out":
        #                 move_type = "out"
        #                 company_id = picking.company_id.id
        #                 partner_id = picking.partner_id.id
        #                 loc_id = warehouse_obj.browse(cr, 1 , picking_warehouse_id).lot_stock_id.id
        #                 loc_dest_id = warehouse_obj.browse(cr, 1 , picking_warehouse_id).partner_id.property_stock_customer.id
        #             elif picking.type=="in":
        #                 move_type = "in"
        #                 company_id = picking.company_id.id
        #                 partner_id = warehouse_obj.browse(cr, 1 , picking_warehouse_id).partner_id.id
        #                 loc_id = warehouse_obj.browse(cr, 1 , picking_warehouse_id).partner_id.property_stock_supplier.id
        #                 loc_dest_id = warehouse_obj.browse(cr, 1 , picking_warehouse_id).lot_stock_id.id
        #         else:
        #             continue
        #         for move in picking.move_lines:
        #             move_obj.write(cr, uid, move.id, {'type':move_type,'location_id':loc_id,'location_dest_id':loc_dest_id,'partner_id':partner_id,'company_id':company_id,})
        #     except:
        #         continue
        # return True

        #Preparing Object which will be used in the function.
        prodlot_obj=self.pool.get('stock.production.lot')
        stock_move_object = self.pool.get('stock.move')
        stock_picking_out_object = self.pool.get('stock.picking.out')
        stock_picking_out_label_object = self.pool.get('stock.picking.out.label')
        res_users_object = self.pool.get('res.users')
        res_company_obj = self.pool.get('res.company')
        user_record = res_users_object.browse(cr,1,1,context=None)
        list_of_status = ['delivered','out_for_delivery','in_transit','pre_transit','label-sent','available_for_pickup','done','ship_queue','assigned','confirmed','auto','draft','return_to_sender','failure','unknown','cancel','exception']

        #Loop on Delivery Orders
        for picking_out_record in self.browse(cr,1,ids):
            worriest_state = ''
        
            #Filter to only check status for Created Labels, Only for EasyPost, and Check if API informations are under Company.
            if picking_out_record.state in ('draft','auto','confirmed','assigned','ship_queue'):
                continue
            if not picking_out_record.carrier_tracking_ref or picking_out_record.carrier_tracking_ref=='':
                continue
            if picking_out_record.shipping_carrier not in ("USPS","UPS","FEDEX"):
                continue

            if not picking_out_record.company_id.easypost_api:
                raise osv.except_osv(_('Message!'),  _("Please enter Shipping Api key inside company : %s" % (picking_out_record.company_id.name)))
            elif picking_out_record.company_id.easypost_prod_vs_staging=="Test":
                raise osv.except_osv(_('Message!'),  _("This functionality is only available for production APIs.\n Please check your company configuration : %s . \n Delivery Order : %s" % (picking_out_record.company_id.name,picking_out_record.name)))
                continue
            else:
                easypost.api_key = picking_out_record.company_id.easypost_api
                


            for move_line in picking_out_record.move_lines:
                #Correct Prodlot_id if it's belonging to an other product.
                _logger.debug('Updating Prodlot_id in move_line %s of delivery_order %s', (move_line.id, picking_out_record.id))

                if move_line.prodlot_id: 
                    if move_line.prodlot_id.product_id.id != move_line.product_id.id:
                        correct_prodlot_ids = prodlot_obj.search(cr, uid, [('name','=',move_line.prodlot_id.name),('product_id','=',move_line.product_id.id)])
                        if correct_prodlot_ids:
                            stock_move_object.write(cr, 1, move_line.id, {'prodlot_id': correct_prodlot_ids[0]},context)
                        else:
                            stock_move_object.write(cr, 1, move_line.id, {'prodlot_id': False},context)
                        
                
                _logger.debug('MOVE ID : %s  |||  Tracker: Carrier %s Tracking Number %s', (move_line.id,move_line.carrier_tracking_ref, picking_out_record.shipping_carrier))
                try:
                    tracker = easypost.Tracker.create(tracking_code=move_line.carrier_tracking_ref,carrier=picking_out_record.shipping_carrier)
                except:
                    continue
                if not tracker['status'] in ('return_to_sender','failure','unknown'):
                    if tracker['status'] in ('available_for_pickup'):
                        stock_move_object.write(cr, 1, move_line.id, {'label_state': 'delivered','state':'done'},context)
                    else:
                        stock_move_object.write(cr, 1, move_line.id, {'label_state': tracker['status'],'state':'done'},context)
                else:
                    stock_move_object.write(cr, 1, move_line.id, {'label_state': 'exception', 'exception_description': tracker['status'],'state':'done'},context)

                #Update Delivery Order with the worriest state of Move Lines
                    #Get the Worriest State of Stock.Moves
                if worriest_state=='' or list_of_status.index(tracker['status']) > list_of_status.index(worriest_state) :
                    worriest_state = tracker['status']
                    #Get the Worriest State of Label_States if it's defined already ( if label is created )
                    if move_line.label_state and move_line.label_state!='':
                        if worriest_state=='' or list_of_status.index(move_line.label_state) > list_of_status.index(worriest_state) :
                            worriest_state = move_line.label_state

                #Now check if there is already the worriest state in delivery order, or put instead of it the new worriest state "worriest_state".
                if not (picking_out_record.state in list_of_status) or (list_of_status.index(picking_out_record.state) < worriest_state):
                    if worriest_state=='available_for_pickup':
                        stock_picking_out_object.write(cr, 1, picking_out_record.id, {'state': 'delivered'}, context)
                    else:
                        stock_picking_out_object.write(cr, 1, picking_out_record.id, {'state': worriest_state}, context)

                elif worriest_state in ('return_to_sender','failure','unknown'):
                    stock_picking_out_object.write(cr, 1, picking_out_record.id, {'state': 'exception', 'exception_description': worriest_state})
                
        return True
       

       
    def cron_create_shipment_simplest(self, cr, uid, ids, context=None):
    
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
        label_types_to_print = []
        label_ids = stock_picking_out_label_object.search(cr, uid, [('is_label_created','=',False)])

        #Loop on Labels.
        concerned_ids = []
        #itteration = 0 # FOR TEST : GENERATE ERROR IN MIDDLE
        for label_record in stock_picking_out_label_object.browse(cr, uid, label_ids):
        
            ##############################################################################################
            # REEL START : Here the reel start of creation of labels, after raising                      #
            # all kinds of exceptions about data entry in the top loops, now any exception               #
            # will be reported in the field easypost_exception in the corresponding delivery order,      #
            # or in the corresponding move_lines.                                                        #
            ##############################################################################################
            
            #Loop on delivery orders of a label
            for picking_out_record in label_record.picking_ids:
                #itteration += 1 # FOR TEST : GENERATE ERROR IN MIDDLE
                stamps_service = False
                continue_picking_too = False  # Boolean to know if Label data build was successful, if not JUMP ALL DELIVERY LABELS.
                shipment = {}   #Dict Containing DATA to create Labels for EasyPost.
                pack_dict = {}  # LIST OF PACKS DATA :
                                # pack_dict = {'pack_id' : ['is_label_created','total_weight',[list_of_move_ids]]}
                pack_list_2_exclude = []    #List of packs to be excluded.
                stamps_tuple = {}   #Dict Containing DATA to create Labels for STAMPS.
                stamps_total_amount_purchase = 0.0
                
                #Check if the Picking Order have a faulty flag ( which means it still not reviewed ). 
                if picking_out_record.faulty:
                    stock_picking_out_object.write(cr,uid,picking_out_record.id,{'state':'ship_queue',})
                    stock_picking_out_label_object.write(cr, uid, label_record.id, {'picking_ids' : [(3,picking_out_record.id )]})
                    _logger.warning('### SAASIER_SHIPPING ### : Something went wrong Picking order have FAULTY FLAG, It\'s made back to SHIPPING_QUEUE, Order_id : %s' % picking_out_record.id)
                    cr.commit()
                    continue
                
                #LOOP IN MOVES AND CREATE pack_dict{}
                
                for move_record in picking_out_record.move_lines:
                    #Jump moves with excluded pack_id:
                    if move_record.tracking_id.id in pack_list_2_exclude:
                        continue
                        
                
                    #1-Check if the label of move (pack) was already created.
                    #2-And exclude the corresponding pack in a list : In case users delete Track_ref for some moves but not from all for same pack,
                    #We jump it so no label will be created untill users delete all tracking_ref from all moves of same pack.
                    if move_record.carrier_tracking_ref:
                        #exclude the pack_id
                        if move_record.tracking_id:
                            pack_list_2_exclude.append(move_record.tracking_id.id)
                        continue
                        
                        
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
                    #This still should be jumped otherwise the key of the package will not found in pack_dict[]
                    if move_record.carrier_tracking_ref:
                        continue
                        
                    #Jump moves with excluded pack_id:
                    if move_record.tracking_id.id in pack_list_2_exclude:
                        continue
                        
                    #Check if the label of pack of this move was already created with a previous move, if yes, jump the iteration.
                    if pack_dict[str(move_record.tracking_id.id)][0]==True:
                        continue
                    else:
                        pack_dict[str(move_record.tracking_id.id)][0]=True
                        

                    #############IF UPS, USPS or FEDEX###################
                    # START CREATING ADDRESSES FOR EASYPOST
                    if picking_out_record.shipping_carrier in ('UPS','USPS','FEDEX'):
                        #THIS IS THE RIGHT PLACE WHERE WE SHOULD CONFIGURE THE EASYPOST API, BECAUSE THERE CAN BE MULTIPLE COMPANIES SELECTED
                        #IN THE LIST, AND THEN AN AUTHENTICATION SHOULD BE DONE EVERY DELIVERY ORDER
                        #CHECK IF THERE IS AN EASYPOST API
                        ups_third_party_account = False
                        #CHECK IF UPS Carrier is choosed, and if Third party is needed
                        if picking_out_record.shipping_carrier=="UPS" and \
                            ( picking_out_record.company_id.ups_third_party_company_id and \
                            picking_out_record.company_id.ups_third_party_account and \
                            picking_out_record.company_id.ups_third_party_country_id and \
                            picking_out_record.company_id.ups_third_party_postal_code):
                            
                            #If everything is ok, get the API key, and set third_party_variables.
                            easypost.api_key = picking_out_record.company_id.ups_third_party_company_id.easypost_api
                            ups_third_party_account = picking_out_record.company_id.ups_third_party_account
                            ups_third_party_country_id = picking_out_record.company_id.ups_third_party_country_id.code
                            ups_third_party_postal_code = picking_out_record.company_id.ups_third_party_postal_code
                        else:
                            easypost.api_key = picking_out_record.company_id.easypost_api

                                
                        # if itteration == 17: #GENERATE ERROR IN MIDDLE
                            # easypost.api_key = 'Make it wrong' #GENERATE ERROR IN MIDDLE
                                
                                
                        #CREATE FROM and TO ADDRESSES
                        try:
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
                        except easypost.Error as e:
                            stock_move_object.write(cr,uid,move_record.id,{'faulty': True, 'error_note': e})
                            stock_picking_out_object.write(cr,uid,picking_out_record.id,{'faulty': True, 'error_note': e})
                            #This variable is used to "continue" the parent loop (picking) into the next one, so that way, we will not process,
                            #a half of delivery and make exceptions in others, so its like this : if exception in a move, jump to next delivery, not only next move.
                            continue_picking_too = True
                            cr.commit()
                            break

                        #Verify FROM and TO ADDRESSES IF USA
                        if picking_out_record.partner_id.country_id.code=="US":
                        
                            #VERIFY TO ADDRESSE
                            if move_record.location_id.partner_id.address_validation:
                                try:
                                    verified_from_address = verified_from_address.verify()
                                except easypost.Error as e:
                                    stock_move_object.write(cr,uid,move_record.id,{'faulty': True, 'error_note': e})
                                    stock_picking_out_object.write(cr,uid,picking_out_record.id,{'faulty': True, 'error_note': e})
                                    continue_picking_too = True
                                    cr.commit()
                                    break
                                    
                            #VERIFY TO ADDRESSE
                            if picking_out_record.partner_id.address_validation:
                                try:
                                    verified_to_address = verified_to_address.verify()
                                except easypost.Error as e:
                                    stock_move_object.write(cr,uid,move_record.id,{'faulty': True, 'error_note': e})
                                    stock_picking_out_object.write(cr,uid,picking_out_record.id,{'faulty': True, 'error_note': e})
                                    continue_picking_too = True
                                    cr.commit()
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
                            stock_move_object.write(cr,uid,move_record.id,{'faulty': True, 'error_note': e})
                            stock_picking_out_object.write(cr,uid,picking_out_record.id,{'faulty': True, 'error_note': e})
                            continue_picking_too = True 
                            cr.commit()
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
                                stock_move_object.write(cr,uid,move_record.id,{'faulty': True, 'error_note': e})
                                stock_picking_out_object.write(cr,uid,picking_out_record.id,{'faulty': True, 'error_note': e})
                                continue_picking_too = True
                                cr.commit()
                                break

                                
                                
                                
                        #Create Customs for EasyPost
                        customs_items = []
                        customs_info = False
                        try:
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
                                ctype_stamps_2_easypost = {'Other':'other',
                                                                  'Document':'documents',
                                                                  'Gift':'gift',
                                                                  'Commercial Sample':'sample',
                                                                  'Merchandise':'merchandise',
                                                                  'Returned Goods':'returned_goods',
                                    }                                    
                                content_type = ctype_stamps_2_easypost[move_record.tracking_id.content_type]

                                
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
                        except easypost.Error as e:
                            stock_move_object.write(cr,uid,move_record.id,{'faulty': True, 'error_note': e})
                            stock_picking_out_object.write(cr,uid,picking_out_record.id,{'faulty': True, 'error_note': e})
                            continue_picking_too = True
                            cr.commit()
                            break
                      
                        #############################################################################################
                        # CREATE SHIPEMENT, in the shipment dictionary, so that way we will not buy any shipement
                        #in a delivery order,
                        #untill we can create all shipments
                        #of the same delivery successfully..
                        #############################################################################################
                        
                        
                        #Check what are the Easypost Options to add.
                        easypost_options = {}
                        if picking_out_record.partner_id.address_validation==False:
                            easypost_options['address_level_validation'] = 0
                        if ups_third_party_account:
                            easypost_options['bill_third_party_account'] = ups_third_party_account
                            easypost_options['bill_third_party_country'] = ups_third_party_country_id
                            easypost_options['bill_third_party_postal_code'] = ups_third_party_postal_code
                        easypost_options['print_custom_1'] = picking_out_record.origin or picking_out_record.name
                        
                        
                        
                        ######################################################################
                        #FINALLY : Create shipements and check if customs are needed or not. #
                        ######################################################################
                        
                        try:
                            if customs_info:
                                shipment[str(move_record.tracking_id.id)] = easypost.Shipment.create(
                                            to_address = verified_to_address,
                                            from_address = verified_from_address,
                                            parcel = parcel,
                                            reference = picking_out_record.origin or picking_out_record.name,
                                            customs_info = customs_info,
                                            options = easypost_options)
                            else:
                                shipment[str(move_record.tracking_id.id)] = easypost.Shipment.create(
                                            to_address = verified_to_address,
                                            from_address = verified_from_address,
                                            parcel = parcel,
                                            reference = picking_out_record.origin or picking_out_record.name,
                                            options = easypost_options)
                        except easypost.Error as e:
                            stock_move_object.write(cr,uid,move_record.id,{'faulty': True, 'error_note': e})
                            stock_picking_out_object.write(cr,uid,picking_out_record.id,{'faulty': True, 'error_note': e})
                            continue_picking_too = True
                            cr.commit()
                            break
                            
                        # assert False, shipment[str(move_record.tracking_id.id)]
                        
                        # Check if there is no rate Proposed for label.
                        if len(shipment[str(move_record.tracking_id.id)].rates)==0:
                            stock_move_object.write(cr,uid,move_record.id,{'faulty': True, 'error_note': 'There is no rate proposed corresponding to your packages and parameters.'})
                            stock_picking_out_object.write(cr,uid,picking_out_record.id,{'faulty': True, 'error_note': 'There is no rate proposed corresponding to your packages and parameters.'})
                            continue_picking_too = True
                            cr.commit()
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
                            cr.commit()
                            break

                            
                    ######START OF COMMENTING STAMPS 

                    elif picking_out_record.shipping_carrier == 'STAMPS': # IF STAMPS
                    
                    
                        #THIS IS THE RIGHT PLACE WHERE WE SHOULD CONFIGURE THE STAMPS API, BECAUSE THERE CAN BE MULTIPLE COMPANIES SELECTED
                        #IN THE LIST, AND THEN AN AUTHENTICATION SHOULD BE DONE EVERY DELIVERY ORDER
                        integration_id = picking_out_record.company_id.stamps_integ_id
                        username = picking_out_record.company_id.stamps_username
                        password = picking_out_record.company_id.stamps_password
                        # if itteration==17: # FOR TEST : GENERATE ERROR IN MIDDLE
                            # username = 'MAKE IT WRONG' # FOR TEST : GENERATE ERROR IN MIDDLE
                        if picking_out_record.company_id.stamps_prod_vs_staging=="Production":
                            stamps_configuration = StampsConfiguration(integration_id=integration_id, username=username, password=password)
                            SampleOnly = False
                        else:
                            stamps_configuration = StampsConfiguration(wsdl="testing",integration_id=integration_id,username=username, password=password)
    	                    # SampleOnly = True  UNCOMMENT THIS TO USE SAMPLE LABELS.
                            SampleOnly = False
                        stamps_service = StampsService(configuration=stamps_configuration)
                    
                    
                        #Creating Addresses for Stamps
                        try:
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

                        except WebFault as e:
                            stock_move_object.write(cr,uid,move_record.id,{'faulty': True, 'error_note': e})
                            stock_picking_out_object.write(cr,uid,picking_out_record.id,{'faulty': True, 'error_note': e})
                            continue_picking_too = True
                            cr.commit()
                            break
                        
                        #Stamps.com do not return exceptions when address has a severe issue ( like zip code missing...etc)
                        #Let's handle this exception manually.
                        if not verified_from_address['CleanseHash'] and not verified_from_address['OverrideHash']:
                            e = 'Stamps-address : Something went wrong when creating From Address with Stamps.com, the From Address is not\
                                            valid and cannot be verified by Stamps.com API.\n \
                                            For more informations you can contact support with this message %s: ' % str(verified_from_address)
                                            
                            stock_move_object.write(cr,uid,move_record.id,{'faulty': True, 'error_note': e})
                            stock_picking_out_object.write(cr,uid,picking_out_record.id,{'faulty': True, 'error_note': e})
                            _logger.warning('### SAASIER_SHIPPING ### : Stamps-address : Something went wrong when creating From Address with Stamps, \
                                            both : CleanseHash and OverrideHash are void after address validation.\
                                            Concerned order_id : %s' % picking_out_record.id)
                                            
                            continue_picking_too = True
                            cr.commit()
                            break
                            
                        if not verified_to_address['CleanseHash'] and not verified_to_address['OverrideHash']:
                            e = 'Stamps-address : Something went wrong when creating To Address with Stamps.com, the From Address is not\
                                            valid and cannot be verified by Stamps.com API.\n \
                                            For more informations you can contact support with this message %s: ' % str(verified_to_address)
                                            
                            stock_move_object.write(cr,uid,move_record.id,{'faulty': True, 'error_note': e})
                            stock_picking_out_object.write(cr,uid,picking_out_record.id,{'faulty': True, 'error_note': e})
                            _logger.warning('### SAASIER_SHIPPING ### : Stamps-address : Something went wrong when creating From Address with Stamps, \
                                            both : CleanseHash and OverrideHash are void after address validation.\
                                            Concerned order_id : %s' % picking_out_record.id)
                                            
                            continue_picking_too = True
                            cr.commit()
                            break
                            
                            
                        #Create Rates for STAMPS
                        try:
                            ret_val = stamps_service.create_shipping()
                            formated_date = datetime.strptime(picking_out_record.date, '%Y-%m-%d %H:%M:%S').date()
                            ret_val.ShipDate = formated_date.isoformat()
                            ret_val.FromZIPCode = verified_from_address['ZIPCode']
                            ret_val.ToZIPCode = verified_to_address['ZIPCode']
                            ret_val.ToState = verified_to_address['State']
                            add_on = stamps_service.create_add_on()
                            add_on.AddOnType = "SC-A-HP"
                            ret_val.AddOns.AddOnV6.append(add_on)
                        except WebFault as e:
                            stock_move_object.write(cr,uid,move_record.id,{'faulty': True, 'error_note': e})
                            stock_picking_out_object.write(cr,uid,picking_out_record.id,{'faulty': True, 'error_note': e})
                            continue_picking_too = True
                            cr.commit()
                            break
                            
                            
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
                            cr.commit()
                            break
                            
                            
                            
                        #Create Customs for STAMPS
                        customs_lines = []
                        customs = False
                        try:
                            if move_record.tracking_id.content_type:
                                for customs_move_id in pack_dict[str(move_record.tracking_id.id)][2]:
                                    customs_line = self.create_customsline(stamps_service, Description = move_record.product_id.name, Quantity = move_record.product_qty, Value = move_record.product_id.list_price, WeightLb = move_record.override_weight, HSTariffNumber = move_record.product_id.HSTariffNumber, CountryOfOrigin = move_record.location_id.partner_id.country_id.code)
                                    customs_lines.append(customs_line)
                                customs = self.create_customs(stamps_service, move_record.tracking_id.content_type, move_record.tracking_id.other_describe,customs_lines) # Both parameters are required
                        except WebFault as e:
                            stock_move_object.write(cr,uid,move_record.id,{'faulty': True, 'error_note': e})
                            stock_picking_out_object.write(cr,uid,picking_out_record.id,{'faulty': True, 'error_note': e})
                            continue_picking_too = True
                            cr.commit()
                            break
                            
                            
                        #Buy with the rate corresponding to the service in delivery order, If no Rate corresponding to it, raise an error, with possible services to choose.
                        shipment_buy_response = False
                        string_list_of_rates = "\n"
                        try:
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
                                    add_on = stamps_service.create_add_on()
                                    add_on.AddOnType = "SC-A-HP"
                                    ret_val.AddOns.AddOnV6.append(add_on)
                                    transaction_id = datetime.now().isoformat()
                                    stamps_tuple[str(move_record.tracking_id.id)] = (verified_from_address, verified_to_address, ret_val,transaction_id, customs)
                                    stamps_total_amount_purchase += float(rate.Amount)
                        except WebFault as e:
                            stock_move_object.write(cr,uid,move_record.id,{'faulty': True, 'error_note': e})
                            stock_picking_out_object.write(cr,uid,picking_out_record.id,{'faulty': True, 'error_note': e})
                            continue_picking_too = True
                            cr.commit()
                            break
                                
                        #Raise the message if no corresponding rate is available for the choosen service in delivery.
                        if not shipment_buy_response: 
                            string_error = 'The selected combination of Carrier, Service and/or Packaging is not available \
                                                                                        for Delivery Order %s. \n TIP: This is the only available rates corresponding to \
                                                                                        your Packing configurations : %s' % (picking_out_record.name ,string_list_of_rates,)
                            stock_move_object.write(cr,uid,move_record.id,{'faulty': True, 'error_note': string_error})
                            stock_picking_out_object.write(cr,uid,picking_out_record.id,{'faulty': True, 'error_note': string_error})
                            continue_picking_too = True
                            cr.commit()
                            break
                            
                            
                    ######END OF COMMENTING STAMPS 
                    elif picking_out_record.shipping_carrier in ('UPS WorldShip CSV','USPS DataPac CSV','Freight'): # IF UPS WorldShip CSV
                        pass
                        
                    cr.commit() # Anyway : commit after move_line ( pack ) loop.
                    
                #Check if there was any "continue" in the previous move line, if yes, jump to the next
                if continue_picking_too:
                    #Something is wrong ith data and API connection for this Order, then :
                    #1- Make it back to shipping Queue
                    #2- Remove it from shipping_label 
                    stock_picking_out_object.write(cr,uid,picking_out_record.id,{'state':'ship_queue',})
                    stock_picking_out_label_object.write(cr, uid, label_record.id, {'picking_ids' : [(3,picking_out_record.id )]})
                    _logger.warning('### SAASIER_SHIPPING ### : Something went wrong no label will be created for order_id : %s' % picking_out_record.id)
                    cr.commit()
                    continue
                elif not stamps_service and picking_out_record.shipping_carrier == 'STAMPS':
                    #Because we check if Labels were already created, sometimes all labels of an order are created, and then, no error in picking and no stamps_service
                    #is confgured, so we just escape the Picking_order.
                    continue
                else:
                    #Buy with the rate corresponding to the service in delivery order, for all moves (packs) of the delivery order.
                    # assert False, pack_dict.keys() + shipment.keys()
                    do_not_create_labels = False
                    
                    #Purchase Balance.
                    try:
                        if picking_out_record.shipping_carrier == 'STAMPS':
                            transaction_id = datetime.now().isoformat()
                            account_informations = stamps_service.get_account()
                            current_balance = account_informations['AccountInfo']['PostageBalance']['AvailablePostage']
                            difference = math.ceil(stamps_total_amount_purchase - float(current_balance))
                            if difference < 0:
                                res_company_obj.write(cr, uid, picking_out_record.company_id.id, {'stamps_balance': - difference}, context)
                                cr.commit()
                            else:
                                if difference<25:
                                    res_company_obj.write(cr, uid, picking_out_record.company_id.id, {'stamps_balance': 25 - difference }, context)
                                    difference = 25.0
                                else:
                                    res_company_obj.write(cr, uid, picking_out_record.company_id.id, {'stamps_balance': 0.0}, context)
                                
                                result = stamps_service.add_postage(difference, transaction_id=transaction_id)
                                transaction_id = result.TransactionID
                                status = stamps_service.create_purchase_status()
                                seconds = 1
                                _logger.debug('### SAASIER_SHIPPING ### : Looping and Purchasing credits for Stamps with a Total amount of  : %s' % str(difference))
                                while result.PurchaseStatus in (status.Pending, status.Processing):
                                    if seconds + 1 >= 60:
                                        raise osv.except_osv(_('Stamps Postage Purchase Timeout!'),  _('Please check the next reasons, or contact Stamps Support for more informations:\n \
                                            - It may be due to connectivity issue with the Stamps server.\n \
                                            - It maybe due to some problemes with your method of payments you configured in stamps.\n \
                                            This is the TransactionID you can contact Stamps support with : %s ' % (str(result.TransactionID),)))
                                    else:
                                        seconds += 1
                                    print "Waiting {0:d} seconds to get status...".format(seconds)
                                    seconds_msg = "### SAASIER_SHIPPING ### : Waiting {0:d} seconds to get status...".format(seconds)
                                    _logger.debug(seconds_msg)
                                    sleep(seconds)
                                    result = stamps_service.get_postage_status(transaction_id)
                                cr.commit()
                    except WebFault as e:
                        stock_picking_out_object.write(cr,uid,picking_out_record.id,{'faulty': True, 'error_note': e})
                        _logger.debug('### SAASIER_SHIPPING ### : Something went wrong couldn\'t purchase balance from stamps for amount : %s' % str(difference))
                        cr.commit()
                        continue
                        
                        
                    email_string_tracking_numbers = ''                  
                    for pack_id in pack_dict.keys():
                        #pack_dict['pack_id'][2] is the list of move_ids of this pack.
                        #At this level let's get rid of any exception for the picking and it's moves.
                        stock_move_object.write(cr,uid,pack_dict[pack_id][2],{'faulty': False, 'error_note': False})
                        stock_picking_out_object.write(cr,uid,picking_out_record.id,{'faulty': False, 'error_note': False})
                        cr.commit()
                        if picking_out_record.shipping_carrier in ('UPS','USPS','FEDEX'): #IF UPS, USPS or FEDEX
                                
                            for shipment_rate in shipment[pack_id].rates:
                                string_list_of_rates += "\n - %s" % (shipment_rate['service'],)
                                
                                if picking_out_record.shipping_carrier=="UPS":
                                    if shipment_rate['service'].upper()==picking_out_record.shipping_service_ups and shipment_rate['carrier']=="UPS":
                                        try:
                                            shipment_buy_response = shipment[pack_id].buy(rate = shipment_rate)
                                            _logger.debug('### SAASIER_SHIPPING ### : Created EasyPost-UPS Label for Picking_id : %s . And Pack_id : %s' % (str(picking_out_record.id),str(pack_id)))
                                        except easypost.Error as e:
                                            stock_move_object.write(cr,uid,pack_dict[pack_id][2],{'faulty': True, 'error_note': e})
                                            stock_picking_out_object.write(cr,uid,picking_out_record.id,{'faulty': True, 'error_note': e})
                                            do_not_create_labels = True
                                            _logger.warning('### SAASIER_SHIPPING ### : Something wrong with label creation for EasyPost-UPS Label for Picking_id : %s . And Pack_id : %s' % (str(picking_out_record.id),str(pack_id)))
                                            cr.commit()
                                            break
                                
                                if picking_out_record.shipping_carrier=="USPS":
                                    if shipment_rate['service'].upper()==picking_out_record.shipping_service_usps and shipment_rate['carrier']=="USPS":
                                        try:
                                            shipment_buy_response = shipment[pack_id].buy(rate = shipment_rate)
                                            _logger.debug('### SAASIER_SHIPPING ### : Created EasyPost-USPS Label for Picking_id : %s . And Pack_id : %s' % (str(picking_out_record.id),str(pack_id)))
                                        except easypost.Error as e:
                                            stock_move_object.write(cr,uid,pack_dict[pack_id][2],{'faulty': True, 'error_note': e})
                                            stock_picking_out_object.write(cr,uid,picking_out_record.id,{'faulty': True, 'error_note': e})
                                            do_not_create_labels = True
                                            _logger.warning('### SAASIER_SHIPPING ### : Something wrong with label creation for EasyPost-USPS Label for Picking_id : %s . And Pack_id : %s' % (str(picking_out_record.id),str(pack_id)))
                                            cr.commit()
                                            break

                                if picking_out_record.shipping_carrier=="FEDEX":
                                    if shipment_rate['service'].upper()==picking_out_record.shipping_service_fedex and shipment_rate['carrier']=="FEDEX":
                                        try:
                                            shipment_buy_response = shipment[pack_id].buy(rate = shipment_rate)
                                            _logger.debug('### SAASIER_SHIPPING ### : Created EasyPost-FEDEX Label for Picking_id : %s . And Pack_id : %s', (str(picking_out_record.id),str(pack_id)))
                                        except easypost.Error as e:
                                            stock_move_object.write(cr,uid,pack_dict[pack_id][2],{'faulty': True, 'error_note': e})
                                            stock_picking_out_object.write(cr,uid,picking_out_record.id,{'faulty': True, 'error_note': e})
                                            do_not_create_labels = True
                                            _logger.warning('### SAASIER_SHIPPING ### : Something wrong with label creationf for EasyPost-FEDEX Label for Picking_id : %s . And Pack_id : %s' % (str(picking_out_record.id),str(pack_id)))
                                            cr.commit()
                                            break
                                            
                            if do_not_create_labels:
                                break
                                
                            #CREATE INSURANCE IF IT IS CHECKED
                            total_insurance_amount = 0
                            for insurance_move_id in pack_dict[pack_id][2]:
                                insurance_move_record = stock_move_object.browse(cr, uid, insurance_move_id)
                                if insurance_move_record.insured:
                                    if insurance_move_record.value_insured!=0:
                                        total_insurance_amount += insurance_move_record.value_insured
                                    else:
                                        total_insurance_amount += insurance_move_record.product_id.list_price
                            
                            
                            if total_insurance_amount>0:
                                try:
                                    shipment[pack_id].insure(amount = total_insurance_amount) 
                                    _logger.debug('### SAASIER_SHIPPING ### : EasyPost - Adding insurance of : %s . For Picking_id : %s . And Pack_id : %s' % (str(total_insurance_amount),str(picking_out_record.id),str(pack_id)))
                                    #UNS : FIX ME : THIS SHOULD BE CRITICAL, IF INSURANCE CAN HAVE BUGS OR EXCEPTIONS, THEN WE NEED TO FORCE AT LEAST
                                    #GETTING THE LABEL, AND GIVE A SOLUTION TO INSURE THE LABEL LATER....??
                                except easypost.Error as e:
                                    stock_move_object.write(cr,uid,pack_dict[pack_id][2],{'faulty': True, 'error_note': e})
                                    stock_picking_out_object.write(cr,uid,picking_out_record.id,{'faulty': True, 'error_note': e})
                                    _logger.warning('### SAASIER_SHIPPING ### : EasyPost - Something wrong with adding insurance of : %s . For Picking_id : %s . And Pack_id : %s' % (str(total_insurance_amount),str(picking_out_record.id),str(pack_id)))
                                    cr.commit()
                                    #1- If insurance failed, do not bread, we should write the label informations in packages anyway,
                                    #2- a button is associated to picking orders which have already labels created, they can process later the insurance,
                                    #   after fixing the exception
                                    
                            if email_string_tracking_numbers=='':
                                email_string_tracking_numbers = str(shipment_buy_response['tracking_code'])
                            else:
                                email_string_tracking_numbers = email_string_tracking_numbers + ',' +str(shipment_buy_response['tracking_code'])

                            #Always this block should be immediatly after labels creations, because its data is critical, and should be saved.
                            url_tracking_ref = False
                            if picking_out_record.shipping_carrier == "UPS":
                                url_tracking_ref = 'http://wwwapps.ups.com/WebTracking/track?track=yes&trackNums=' + shipment_buy_response['tracking_code'] +'&loc=en_us'
                            elif picking_out_record.shipping_carrier == "USPS":
                                url_tracking_ref = 'https://tools.usps.com/go/TrackConfirmAction?tLabels=' + shipment_buy_response['tracking_code']
                            elif picking_out_record.shipping_carrier == "FEDEX":
                                url_tracking_ref = 'https://www.fedex.com/fedextrack/WTRK/index.html?action=track&trackingnumber=' + shipment_buy_response['tracking_code']
                            stock_move_object.write(cr,uid,pack_dict[pack_id][2],{'url_tracking_ref': url_tracking_ref,'carrier_tracking_ref': shipment_buy_response['tracking_code']})
                            stock_picking_out_object.write(cr,uid,picking_out_record.id,{'url_tracking_ref': url_tracking_ref,'carrier_tracking_ref': shipment_buy_response['tracking_code']})
                            cr.commit()
                            
                            #Prepare data for image and attachement creation.
                            image_url=False
                            image_url=shipment_buy_response.postage_label.label_url
                            resource = urllib.urlopen(image_url) 
                            data = base64.encodestring(resource.read())
                            
                        elif picking_out_record.shipping_carrier == 'STAMPS': #IF STAMPS
                            try:
                                if stamps_tuple[pack_id][4]:
                                    label = stamps_service.get_label_customised_4_customs(stamps_tuple[pack_id][0], 
                                                                                       stamps_tuple[pack_id][1], 
                                                                                       stamps_tuple[pack_id][2],
                                                                                       stamps_tuple[pack_id][3],
                                                                                       stamps_tuple[pack_id][4],
                                                                                       picking_out_record.origin or picking_out_record.name,
                                                                                       sample=SampleOnly)
                                else:
                                    label = stamps_service.get_label_customised(stamps_tuple[pack_id][0], 
                                                                                       stamps_tuple[pack_id][1], 
                                                                                       stamps_tuple[pack_id][2],
                                                                                       stamps_tuple[pack_id][3],
                                                                                       picking_out_record.origin or picking_out_record.name,
                                                                                       sample=SampleOnly)
                                _logger.debug('### SAASIER_SHIPPING ### : Created STAMPS Label for Picking_id : %s . And Pack_id : %s' % (str(picking_out_record.id),str(pack_id)))

                            except WebFault as e:
                                stock_move_object.write(cr,uid,move_record.id,{'faulty': True, 'error_note': e})
                                stock_picking_out_object.write(cr,uid,picking_out_record.id,{'faulty': True, 'error_note': e})
                                cr.commit()
                                do_not_create_labels = True
                                _logger.warning('### SAASIER_SHIPPING ### : Something wrong with creating STAMPS Label for Picking_id : %s . And Pack_id : %s' % (str(picking_out_record.id),str(pack_id)))
                                
                            if do_not_create_labels:
                                break
                            # assert False, type(label.TrackingNumber)
                            stamps_label_tracking_id = label.TrackingNumber
                            stamps_label_URL = label.URL
                            url_tracking_ref = False
                            url_tracking_ref = 'https://tools.usps.com/go/TrackConfirmAction?tLabels=' + stamps_label_tracking_id
                            stock_move_object.write(cr,uid,pack_dict[pack_id][2],{'url_tracking_ref': url_tracking_ref,'carrier_tracking_ref': stamps_label_tracking_id})
                            stock_picking_out_object.write(cr,uid,picking_out_record.id,{'url_tracking_ref': url_tracking_ref,'carrier_tracking_ref': stamps_label_tracking_id})
                            cr.commit()
                            #Prepare data for image and attachement creation.
                            image_url=stamps_label_URL
                            resource = urllib.urlopen(image_url) 
                            data = base64.encodestring(resource.read()) 
                            if email_string_tracking_numbers=='':
                                email_string_tracking_numbers = str(stamps_label_tracking_id)
                            else:
                                email_string_tracking_numbers = email_string_tracking_numbers + ',' +str(stamps_label_tracking_id)
                                
                        elif picking_out_record.shipping_carrier == 'UPS WorldShip CSV':
                            image_url = False
                            data = False
                            
                        elif picking_out_record.shipping_carrier == 'USPS DataPac CSV':
                            image_url = False
                            data = False
                            
                        elif picking_out_record.shipping_carrier == 'Freight':
                            image_url = False
                            data = False
                            
                        #CREATING AND AFFECTING LABELS FOR EVERY MOVE LINE.  
                        for pack_move_id in pack_dict[pack_id][2]:
                            if picking_out_record.shipping_carrier not in ('UPS WorldShip CSV','USPS DataPac CSV','Freight'):
                                attached_already = attach_object.search(cr, uid, [('res_id','=',pack_move_id)])
                                attach_object.unlink(cr, uid, attached_already, context=None)
                                a_id = attach_object.create(cr, uid, {'name': image_url, 'res_model': 'stock.move','res_id': pack_move_id,'type': 'binary', 'db_datas':data })
                                cr.commit()
                        
                        cr.commit() #Anyway : Commit anything after Label Creation of a Pack
                        
                        
                    #If there was an error while creating LABELS for Packs, don't create Picking_Labels, and do not close moves, and deliveries
                    if do_not_create_labels:
                        #Something is wrong ith data and API connection for this Order, then :
                        #1- Make it back to shipping Queue
                        #2- Remove it from shipping_label 
                        stock_picking_out_object.write(cr,uid,picking_out_record.id,{'state':'ship_queue',})
                        stock_picking_out_label_object.write(cr, uid, label_record.id, {'picking_ids' : [(3,picking_out_record.id )]})
                        _logger.error('### SAASIER_SHIPPING ### : /!\ /!\ /!\ Something went wrong labels creationg was stopped in middle for order_id : %s . /!\ /!\ /!\ ' % str(picking_out_record.id))
                        cr.commit()
                        continue
                    else: 
                        #CREATE PICKING_OUT_LABEL CORESPONDING TO ALL CREATED LABELS IN THIS DELIVERY, 
                        #AND IT'S BY WAREHOUSE: CREATE ONE RECORD FOR EVERY WAREHOUSE, AND EVERY RECORD WILL CONTAIN CORRESPONDING DELIVERY ORDERS
                        stock_picking_out_object.write(cr,uid,picking_out_record.id,{'state':'done',
                                                                                     'list_tracking_numbers': email_string_tracking_numbers,
                                                                                     })
                        cr.commit()
                        
                        if email_string_tracking_numbers!='':
                            stock_picking_out_object.send_email_tracking(cr, uid,picking_out_record.id)
                            _logger.debug('### SAASIER_SHIPPING ### : Sending Label Creation Email for Pick_id: %s .' % str(picking_out_record.id))
                
                cr.commit() # Anyway : commit after one Picking_loop
            
            #--->If no interruption of the Label_record processing, then Flag shipping_label as its labels are already created
            #1- If there have been exceptions, then also this command will be executed,
            #2- If there have been interruption which is not handeled with exceptions, then the label will not be mentioned as created,
            #   and the next cron launch will take care of it, no worry about packages with labels created, they will be jumped.
            
            stock_picking_out_label_object.write(cr, uid, label_record.id,{'is_label_created':True})
            cr.commit()
            
        #After finishing looping, check if any label with 0 record and delete it.
        label_to_delete_ids = stock_picking_out_label_object.search(cr, uid, [('picking_ids','=',False)])
        if label_to_delete_ids:
            stock_picking_out_label_object.unlink(cr, uid, label_to_delete_ids)
        cr.commit()
        
        return True
       
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
        label_types_to_print = []
        
        #####################################################################################################################
        # TECHNICAL LOOP: HERE WE WILL SET A DICTIONARY CONTAINING PACKS, WEIGHTS, AND CORRESPONDING MOVE_IDS               #
        # AND WE WILL CONTROLE ALL DATA SETTED IN ALL DELIVERY ORDERS SELECTED IN SHIPPING QUEUE, AND THIS TO RAISE ERRORS  #
        # BEFORE STARTING COMMUNICATION WITH EASYPOST API, AND MAKE IT LIKE AN INITIAL CONTROLE, AFTER THIS LOOP,           #
        # ALL OTHER EXCEPTIONS                                                                                              #
        # WILL BE EASYPOST EXCEPTIONS AND ERRORS, AND THEY WILL BE SAVED IN THE FIELDS "EASYPOST_EXCEPTION".                #
        #####################################################################################################################
        
        #Technical loop in delivery orders.
        for picking_out_record in self.browse(cr,uid,ids):
        
            #Build the list of kinds of labels to be printed later.
            if picking_out_record.shipping_carrier in ('UPS','USPS','FEDEX','STAMPS'):
                label_types_to_print.append('Label Template')
                if not picking_out_record.company_id.label_template_id:
                    raise osv.except_osv(_('Warning !'),  _("You should define a Label Template for : " + str(picking_out_record.company_id.name) + " company."))
            elif picking_out_record.shipping_carrier == 'UPS WorldShip CSV':
                label_types_to_print.append('UPS WorldShip CSV')
            elif picking_out_record.shipping_carrier == 'USPS DataPac CSV':
                label_types_to_print.append('USPS DataPac CSV')
            elif picking_out_record.shipping_carrier == 'Freight':
                label_types_to_print.append('Freight')
                
            #CHECK IF THERE IS AN EASYPOST API, or STAMPS, FOR CURRENT COMPANY OF THE DELIVERY ORDER,
            #then configure all services of API.
            if picking_out_record.shipping_carrier=="STAMPS" and \
                ( not picking_out_record.company_id.stamps_integ_id or not picking_out_record.company_id.stamps_username or not picking_out_record.company_id.stamps_password):
                raise osv.except_osv(_('Message!'),  _("Please enter Integration Details for Stamps inside company !"))

            if picking_out_record.shipping_carrier in ("USPS","UPS","FEDEX") and \
                ( not picking_out_record.company_id.easypost_api):
                
                #CHECK IF UPS Carrier is choosed, and if Third party is needed : It is the API of the parent company
                if picking_out_record.shipping_carrier=="UPS" and \
                    ( picking_out_record.company_id.ups_third_party_company_id and picking_out_record.company_id.ups_third_party_account and  picking_out_record.company_id.ups_third_party_country_id and picking_out_record.company_id.ups_third_party_postal_code):
                    #Check if no API is setup for the API company provider
                    if not picking_out_record.company_id.ups_third_party_company_id.easypost_api:
                        raise osv.except_osv(_('Message!'),  _("Please enter Shipping Api key inside the company that provides API key !"))
                else:
                    raise osv.except_osv(_('Message!'),  _("Please enter Shipping Api key inside related company of this delivery order!"))
                    
                    
                    
            ##################################################################################
            #  Start of control of data entry in all selected deliveries and all move lines  #
            ##################################################################################
            
            for move_record in picking_out_record.move_lines:
                #IF there was already a label created for move, jump it
                if move_record.carrier_tracking_ref:
                    continue

                string_error_indicator = '\n\n- Concerned Delivery Order is : %s .\n - Concerned Pack is : %s' \
                                                            % (str(picking_out_record.name), str(move_record.tracking_id.name))
                                                            
                if move_record.tracking_id.content_type:
                    if not move_record.product_id.HSTariffNumber:
                        raise osv.except_osv(_('Warning!'),  _('HS Tariff Number is required for products with customs activated.\n -Concerned Product : %s\
                                                                                    %s ' % (move_record.product_id.name,string_error_indicator,)))
                    if not move_record.product_id.list_price: 
                        raise osv.except_osv(_('Warning!'),  _('Sale Price is required for products with customs activated.\n -Concerned Product : %s\
                                                                                    %s ' % (move_record.product_id.name,string_error_indicator,)))
                                                                                    
                if move_record.product_id.list_price==0 and move_record.value_insured==0 and move_record.insured:
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
                                                                                                            
                if picking_out_record.shipping_carrier=="STAMPS" and not picking_out_record.shipping_packaging_usps_stamps and not move_record.tracking_id.box_setup_id:
                    raise osv.except_osv(_('Message!'),  _('Please, choose one of the predefined Shipping Packing for STAMPS in your delivery order, \
                                                                                                            or select Box Setup inside every move line (pack) of this delivery order  !\
                                                                                                            %s ' % (string_error_indicator,)))
               
                #Check if shipping service is choosed
                if picking_out_record.shipping_carrier=="UPS" and not picking_out_record.shipping_service_ups:
                    raise osv.except_osv(_('Message!'),  _("Please, choose one of the Shipping Services for UPS in your delivery order !\
                                                                                %s " % (string_error_indicator,)))
                    
                if picking_out_record.shipping_carrier=="USPS" and not picking_out_record.shipping_service_usps:
                    raise osv.except_osv(_('Message!'),  _("Please, choose one of the Shipping Services for USPS in your delivery order !\
                                                                                %s " % (string_error_indicator,)))
                    
                if picking_out_record.shipping_carrier=="FEDEX" and not picking_out_record.shipping_service_fedex:
                    raise osv.except_osv(_('Message!'),  _("Please, choose one of the Shipping Services for FEDEX in your delivery order !\
                                                                               %s " % (string_error_indicator,)))
                    
                if picking_out_record.shipping_carrier=="STAMPS" and not picking_out_record.shipping_service_usps_stamps:
                    raise osv.except_osv(_('Message!'),  _("Please, choose one of the Shipping Services for STAMPS in your delivery order !\
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
          
                if picking_out_record.shipping_carrier=="STAMPS" and ( not picking_out_record.shipping_packaging_usps_stamps or picking_out_record.shipping_packaging_usps_stamps=="Package") \
                and (not move_record.tracking_id.box_setup_id.length or not move_record.tracking_id.box_setup_id.width or not\
                move_record.tracking_id.box_setup_id.height or move_record.override_weight==0.0):
                    raise osv.except_osv(_('Message!'),  _("Please, choose one of the predefined Shipping Packing for STAMPS in your delivery order, \
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
                                                                                                  
            ##################################################################################
            #    End of control of data entry in all selected deliveries and all move lines  #
            ##################################################################################
                    

                    
        ###### CHEK IF THERE IS MULTIPLE TYPES OF LABELS TO GENERATE ( Label Template, WorldShip, Datapac..etc )
        is_same_type = all(x == label_types_to_print[0] for x in label_types_to_print)
        if not is_same_type:
            raise osv.except_osv(_('Multiple Shipping Labels type used !'),  _("You cannot combine Shipping Labels type like using : Printed Labels and WorldShip CSV."))
        
        
        
        
        ##############################################################################################
        # REEL START : Here the reel start of creation of labels, after raising                      #
        # all kinds of exceptions about data entry in the top loops, now any exception               #
        # will be reported in the field easypost_exception in the corresponding delivery order,      #
        # or in the corresponding move_lines.                                                        #
        ##############################################################################################
                            
        for picking_out_record in self.browse(cr,uid,ids):
            #Change delivery order state to done
            stock_picking_out_object.write(cr,uid,picking_out_record.id,{'state':'done',})
            #Create Shipping Label records : one label for every location
            if str(move_record.location_id.id) in list(warehouse_ids_dict.keys()):
                stock_picking_out_label_id = stock_picking_out_label_object.write(cr,uid,warehouse_ids_dict[str(move_record.location_id.id)],\
                                                                            {'picking_ids':[(4,picking_out_record.id)]})
            else:
                stock_picking_out_label_id = stock_picking_out_label_object.create(cr,uid,{'date':time.strftime('%Y-%m-%d %H:%M:%S'),\
                                                                           'location_id':move_record.location_id.id,\
                                                                           'label_template_id':picking_out_record.company_id.label_template_id.id,\
                                                                           'labels_type':label_types_to_print[0],\
                                                                           'picking_ids':[(4,picking_out_record.id)]})
                string_warehouse_id = str(move_record.location_id.id)
                warehouse_ids_dict[string_warehouse_id]=stock_picking_out_label_id

        
        #Check if the Cron is desactivated then RUN The function which creates labels immediatly.
        base_config_obj = self.pool.get('base.config.settings')
        base_config_ids = base_config_obj.search(cr, uid, [('id','!=',0)],context=context)
        base_config_record = base_config_obj.browse(cr, uid, max(base_config_ids),context)
        if base_config_record.default_use_saasier_shipping_cron==False:
            self.cron_create_shipment_simplest(cr, uid, ids, context)
            
        return True
        
    def send_email_tracking(self, cr, uid, ids, context=None):
        email_template_obj = self.pool.get('email.template')
        ir_model_data = self.pool.get('ir.model.data')
        try:
            template_id = ir_model_data.get_object_reference(cr, uid, 'saasier_shipping', 'email_template_edi_stock_picking_tracker')[1]
        except ValueError:
            template_id = False
        if template_id:
            email_id = email_template_obj.send_mail(cr, uid, template_id, ids, force_send=True, context=context)
        return True


    def send_email_new_state(self, cr, uid, ids, context=None):
        email_template_obj = self.pool.get('email.template')
        ir_model_data = self.pool.get('ir.model.data')
        try:
            template_id = ir_model_data.get_object_reference(cr, uid, 'saasier_shipping', 'email_template_edi_stock_picking_tracker_update_status')[1]
        except ValueError:
            template_id = False
        if template_id:
            email_id = email_template_obj.send_mail(cr, uid, template_id, ids, force_send=True, context=context)
        return True

    def check_new_state_and_email(self, cr, uid, ids, context=None):
        move_obj = self.pool.get('stock.move')
        move_ids = move_obj.search(cr, uid, [('carrier_tracking_ref','not in',(False,'')),('is_state_changed','=',True)],context=context)
     
        picking_ids_sent = []
        # assert False, move_ids
        for found_move in move_obj.browse(cr,uid, move_ids,context):
            if found_move.picking_id.id not in picking_ids_sent:
                picking_ids_sent.append(found_move.picking_id.id)
                self.send_email_new_state(cr, uid, found_move.picking_id.id,context)
                move_obj.write(cr, uid, found_move.id, {'last_status_emailed':found_move.label_state,})
        return True

stock_picking()
