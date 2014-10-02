# -*- coding: utf-8 -*-
##############################################################################
#
#    OpenERP, Open Source Management Solution
#    Copyright (C) 2004-2010 Tiny SPRL (<http://tiny.be>).
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
from openerp.osv import fields, osv
import time


class oe_evernote(osv.osv):
    _name = 'oe.evernote'
    _columns = {
        'name': fields.char('name', size=64,),
        'link': fields.char('Link'),
        'date': fields.datetime('Date', readonly=True),
        'desc': fields.text('Description'),
        'po_id': fields.many2one('purchase.order', 'Purchase order'),
        'crm_id': fields.many2one('crm.lead', 'Lead/Opportunity'),
        'partner_id': fields.many2one('res.partner', 'Partner'),
        'invoice_id': fields.many2one('account.invoice', 'Invoice'),
        'project_id': fields.many2one('project.project', 'Project'),
        'task_id': fields.many2one('project.task', 'Task'),
        'statement_id': fields.many2one('account.bank.statement',
                                        'Bank Statement'),
        'acc_move_id': fields.many2one('account.move', 'Account Move'),
        'acc_voucher_id': fields.many2one('account.voucher',
                                          'Account Voucher'),
        'picking_id': fields.many2one('stock.picking.in', 'Incoming Shipment'),
        'outgoing_id': fields.many2one('stock.picking.out', 'Delivery Order'),
        'inventory_id': fields.many2one('stock.inventory', 'Inventory'),
        'mo_id': fields.many2one('mrp.production', 'Manufacturing Order'),
        'product_id': fields.many2one('product.product', 'Product'),
        'employee_id': fields.many2one('hr.employee', 'Employee'),
        'timesheet_id': fields.many2one('hr_timesheet_sheet.sheet',
                                        'Timesheet')
    }
    _defaults = {
        'date': time.strftime("%Y-%m-%d %H:%M:%S"),
    }
oe_evernote()


class purchase_order(osv.osv):
    _inherit = 'purchase.order'
    _columns = {
        'evernote_ids': fields.one2many('oe.evernote', 'po_id',
                                        string="evernote")
    }
purchase_order()


class crm_lead(osv.osv):
    _inherit = 'crm.lead'
    _columns = {
        'evernote_ids': fields.one2many('oe.evernote', 'crm_id',
                                        string="evernote")
    }
crm_lead()


class res_partner(osv.osv):
    _inherit = 'res.partner'
    _columns = {
        'evernote_ids': fields.one2many('oe.evernote', 'partner_id',
                                        string="evernote")
    }
res_partner()


class account_invoice(osv.osv):
    _inherit = 'account.invoice'
    _columns = {
        'evernote_ids': fields.one2many('oe.evernote', 'invoice_id',
                                        string="evernote")
    }
account_invoice()


class project_project(osv.osv):
    _inherit = 'project.project'
    _columns = {
        'evernote_ids': fields.one2many('oe.evernote', 'project_id',
                                        string="evernote")
    }
project_project()


class project_task(osv.osv):
    _inherit = 'project.task'
    _columns = {
        'evernote_ids': fields.one2many('oe.evernote', 'task_id',
                                        string="evernote")
    }
project_task()


class account_bank_statement(osv.osv):
    _inherit = 'account.bank.statement'
    _columns = {
        'evernote_ids': fields.one2many('oe.evernote', 'statement_id',
                                        string="evernote")
    }
account_bank_statement()


class account_move(osv.osv):
    _inherit = 'account.move'
    _columns = {
        'evernote_ids': fields.one2many('oe.evernote', 'acc_move_id',
                                        string="evernote")
    }
account_move()


class account_voucher(osv.osv):
    _inherit = 'account.voucher'
    _columns = {
        'evernote_ids': fields.one2many('oe.evernote', 'acc_voucher_id',
                                        string="evernote")
    }
account_voucher()


class stock_picking_in(osv.osv):
    _inherit = 'stock.picking.in'
    _columns = {
        'evernote_ids': fields.one2many('oe.evernote', 'picking_id',
                                        string="evernote")
    }
stock_picking_in()


class stock_picking_out(osv.osv):
    _inherit = 'stock.picking.out'
    _columns = {
        'evernote_ids': fields.one2many('oe.evernote', 'outgoing_id',
                                        string="evernote")
    }
stock_picking_out()


class stock_picking(osv.osv):
    _inherit = 'stock.picking'
    _columns = {
        'evernote_ids': fields.one2many('oe.evernote', 'picking_id',
                                        string="evernote")
    }
stock_picking()


class stock_inventory(osv.osv):
    _inherit = 'stock.inventory'
    _columns = {
        'evernote_ids': fields.one2many('oe.evernote', 'inventory_id',
                                        string="evernote")
    }
stock_inventory()


class mrp_production(osv.osv):
    _inherit = 'mrp.production'
    _columns = {
        'evernote_ids': fields.one2many('oe.evernote', 'mo_id',
                                        string="evernote")
    }
mrp_production()


class product_product(osv.osv):
    _inherit = 'product.product'
    _columns = {
        'evernote_ids': fields.one2many('oe.evernote', 'product_id',
                                        string="evernote")
    }
product_product()


class hr_employee(osv.osv):
    _inherit = 'hr.employee'
    _columns = {
        'evernote_ids': fields.one2many('oe.evernote', 'employee_id',
                                        string="evernote")
    }
hr_employee()


class hr_timesheet_sheet(osv.osv):
    _inherit = 'hr_timesheet_sheet.sheet'
    _columns = {
        'evernote_ids': fields.one2many('oe.evernote', 'timesheet_id',
                                        string="evernote")
    }
hr_timesheet_sheet()
