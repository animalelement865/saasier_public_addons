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
import time
from openerp.osv import osv, fields
from openerp import netsvc
from tools.translate import _
import logging
logger = netsvc.Logger()


class product_packaging(osv.osv):
    _inherit = 'product.packaging'
    _columns = {
        'ul': fields.many2one('product.ul', 'Type of package', required=False),
        'box_setup_id' : fields.many2one('box.setup','Box Setup'),
    }
product_packaging()
