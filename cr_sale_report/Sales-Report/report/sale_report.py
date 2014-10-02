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

import time

from openerp.report import report_sxw

class sale_details(report_sxw.rml_parse):
    def __init__(self, cr, uid, name, context=None):
        self.total_amount = 0.00
        self.net_total=0.00
        self.res={}
        self.category_ids = []
        super(sale_details, self).__init__(cr, uid, name, context=context)
        self.localcontext.update({
            'time': time, 
            'get_product': self._get_product,
            'get_total_amount': self._get_total_amount,
            'get_product_category':self._get_product_category,
            
            'get_rec':self._get_vals,
            'get_parent':self._get_parent,
            'get_main':self._get_main,
            
            'get_category_ids' : self._get_category_ids,
            
            'get_category_name' : self._get_category_name,
            'get_product_details': self._get_prdouct_details,
            'get_category_total': self._get_category_total,
            'calculate': self._calculate,
            
        })


        
    def _get_category_ids(self, category_id):
        category_ids=[]
        category = self.pool.get('product.category').browse(self.cr, self.uid, [category_id])[0]
        if category.type=='normal':
            category_ids.append(category.id)
        for child in category.child_id:
            if child.type=='view':
                category_ids+= self._get_category_ids(child.id)
            else:
                category_ids+=[child.id]
        self.category_ids = category_ids
        return category_ids
            
    def _get_category_name(self, category_id):
        category = self.pool.get('product.category').browse(self.cr, self.uid, [category_id])[0]
        return category.complete_name
    
    def _calculate(self):
        if self.category_ids:
            total_amount = 0.00
            for category_id in self.category_ids:
                product_lines = self._get_prdouct_details(category_id)
                for product_line in product_lines:
                    total_amount+=product_line['amount']
        self.total_amount = total_amount
            
        

    def _get_prdouct_details(self, category_id):
        product_ids = self.pool.get('product.product').search(self.cr, self.uid, [('categ_id','=',category_id)])
        lines = []
        self.res[category_id]={}
        self.res[category_id]['total_qty']=0.00
        self.res[category_id]['total_amt']=0.00
        for product_id in product_ids:
            line = {}
            product=self.pool.get('product.product').browse(self.cr, self.uid, [product_id] )[0]
            line['product_name'] = product.name
            sale_line_ids = self.pool.get('sale.order.line').search(self.cr, self.uid, [('product_id','=',product.id),('state','not in',['cancel','draft'])])
            qty=0.00
            amount=0.00
            for sale_line_id in sale_line_ids:
                sale_line = self.pool.get('sale.order.line').browse(self.cr, self.uid, [sale_line_id])[0]
                amount+=sale_line.price_subtotal
                qty+=sale_line.product_uom_qty
            average=0.00
            if qty:
                average=amount/qty
            line['category_id'] = category_id
            line['product_id'] = product_id
            line['average']=average
            line['amount']=amount
            line['qty']=qty
            line['percentage']=0.00
            if self.total_amount and amount:
                line['percentage'] = (amount / self.total_amount) * 100
            self.res[category_id]['total_qty']+=qty
            self.res[category_id]['total_amt']+=amount
            lines.append(line)
        return lines
    
    def _get_category_total(self, category_id):
        if category_id in self.res:
            return self.res[category_id]
        return {category_id : {'total_qty':0.00,'total_amt':00}}
        


    def _get_product_category(self,category_id):
        categ_obj=self.pool.get('product.category')
        product_category=''
        if category_id:
            product_category=categ_obj.browse(self.cr,self.uid,category_id).name
        return product_category
    
    def _get_product(self, form):
        self.total_amount = 0.00
        product_brw=self.pool.get('product.product')
        product_ids=product_brw.search(self.cr,self.uid,[('categ_id','child_of',[form['category_id'][0]])])
        product_val = []
        sl_no = 0
        if product_ids:
            self.cr.execute(""" select prd_template.name,sum(line.product_uom_qty),sum((line.product_uom_qty * line.price_unit)* (1 - (line.discount)/100)) from sale_order_line as line 
                                inner join sale_order as sale on sale.id = line.order_id
                                inner join product_product as product on product.id = line.product_id
                                inner join product_template as prd_template on prd_template.id = product.product_tmpl_id
                                where 
                                product_id in (%s) and 
                                date(sale.date_order) >= '%s' and 
                                date(sale.date_order) <= '%s' and 
                                sale.state not in ('draft','cancel')
                                group by line.product_id,prd_template.name"""%(','.join(map(str,product_ids)),form['date_from'],form['date_to']))
            data =  self.cr.fetchall()
            for val in data:
                    sl_no += 1
                    product_val.append({
                                        'sr_no':sl_no,
                                        'name':val[0],
                                        'qty':val[1],
                                        'amount':val[2]
                                        })
                    self.total_amount += val[2]
        return product_val
    
    
    
    def _get_total_amount(self):
        return self.total_amount or 0.00
    
    
    def _get_main(self,cat_id):
        cat_obj=self.pool.get('product.category')
        if cat_id:
            return [cat_id]
        return cat_obj.search(self.cr,self.uid,[('type','=','view')])
    def _get_parent(self,parent_cat_id):
        cat_obj=self.pool.get('product.category')
        result = parent_cat_id and at_obj.browse(self.cr, self.uid, parent_cat_id).name or ""
        return result
        
    def _get_vals(self, start_date, end_date, type, product_ids, parent_cat_id):
        categ_obj = self.pool.get('product.category')
        pro_obj = self.pool.get('product.product')
        if type == 'categ':
            category_ids = categ_obj.search(self.cr, self.uid, [('parent_id', 'child_of', [parent_cat_id])])
            product_ids = pro_obj.search(self.cr, self.uid, [('categ_id', 'in', category_ids)])
        final_res=[]
        if product_ids:
            rec_dict={}
            ls_ids=[]
            self.cr.execute("select pt.name,p.default_code,sum(l.quantity)*(sum(l.quantity*l.price_unit)/sum(l.quantity)) as subtotal,    \
                            sum(l.quantity) as tot_qty,sum(l.quantity*l.price_unit)/sum(l.quantity) as avg,pt.categ_id, pu.name  as uom   \
                            from account_invoice i left join account_invoice_line l on i.id=l.invoice_id     \
                                              left join product_product p on l.product_id=p.id    \
                                              left join product_template pt on p.product_tmpl_id=pt.id    \
                                              left join product_uom pu on pu.id=l.uos_id    \
                       where i.date_invoice>='%s' and i.date_invoice<='%s' and i.state in ('open','paid') and p.id in (%s)  and  \
                       i.type='out_invoice' group by pt.categ_id,pt.name,p.default_code,pu.name " % (start_date, end_date, ','.join([str(x) for x in product_ids])))

            ls_ids = self.cr.fetchall()
            rec_dict['rec'] = ls_ids
            final_res.append(rec_dict)
        return final_res     
        

report_sxw.report_sxw('report.sale.details', 'sale.order', '', parser=sale_details)


# vim:expandtab:smartindent:tabstop=4:softtabstop=4:shiftwidth=4:
