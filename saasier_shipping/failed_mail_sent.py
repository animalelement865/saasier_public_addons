# -*- coding: utf-8 -*-
##############################################################################
#
#    OpenERP, Open Source Management Solution
#    Copyright (C) 2014-Today Genpex (<http://http://www.genpex.com>).
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
import openerp.addons.decimal_precision as dp
from openerp.tools.translate import _

class mail_mail(osv.Model):
    _inherit = 'mail.mail'
    _columns = {
            }
    def check_failed_mail(self, cr, uid, context=None):
        print"\n\n\nCaleedd............................................"
        mail_ids = self.search(cr, uid, [('state','=','exception')])
        print"\n\nmail_ids",mail_ids
        if mail_ids:
	        for mail_id in mail_ids:
		        send = self.send(cr, uid, [mail_id], auto_commit=False, recipient_ids=None, context=context)
		        print "\n\n**********Mail Forcefully Send==============",send
        return True
    
mail_mail()


# vim:expandtab:smartindent:tabstop=4:softtabstop=4:shiftwidth=4:
