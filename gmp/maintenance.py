from osv import osv,fields
from datetime import datetime
from time import mktime
import time
from dateutil.relativedelta import relativedelta

class mro_order(osv.osv):
    _inherit    = "mro.order"
    _columns    = {
                   'recurring_id'       : fields.many2one('mro.recurring','Reference'),
                   'responsible_id'     : fields.many2one('hr.employee','Responsible'),
                   'sop_ids'            : fields.many2many('document.page','order_sop_rel','order_id','sop_id','SOP'),
                   }
mro_order()

class mro_task(osv.osv):
    _inherit    = "mro.task"
    _columns    = {
                   'sop_ids'            : fields.many2many('document.page','task_sop_rel','task_id','sop_id','SOP'),
                   'mo_id'              : fields.many2one('mro.order','Related Maintenance Order',ondelete="cascade"),
                   }
mro_task()

class mro_recurring(osv.osv):
    _name       = "mro.recurring"
    _description= "Maintenance Recuring"

    MAINTENANCE_PERIOD = [
        ('days','Days'),
        ('weeks','Weeks'),
        ('months','Months'),
        ('quarters','Quarters'),
        ('years','Years')
    ]

    MAINTENANCE_TYPE_SELECTION = [
        ('bm', 'Breakdown'),
        ('cm', 'Corrective'),
        ('pm','Preventive')
    ]
    
    _columns    = {
                   'name'               : fields.char('Description',size=200,required=True),
                   'asset_id'           : fields.many2one('asset.asset','Equipment',required=True),
                   'frequency'          : fields.selection([('daily','Daily'),
                                                            ('weekly','Weekly'),
                                                            ('monthly','Monthly'),
                                                            ('quarterly','Quarterly'),
                                                            ('yearly','Yearly')],'Frequency'),
                   'sop_ids'            : fields.many2many('document.page','recurring_sop_rel','recurring_id','sop_id','SOP'),
                   'mro_order_ids'      : fields.one2many('mro.order','recurring_id','Maintenance Order'),
                   'responsible_id'     : fields.many2one('hr.employee','Responsible'),
                   'location_id'        : fields.many2one('stock.location','Parts Location'),
                   'maintenance_type'   : fields.selection(MAINTENANCE_TYPE_SELECTION, 'Maintenance Type', required=True),
                   'first_schedule'     : fields.datetime('First Schedule',help='Define when does your sets of maintenance will be started'),
                   'recurring_amount'   : fields.integer('Amount Recurring'),
                   'recurring_period'   : fields.selection(MAINTENANCE_PERIOD, 'Period Recurring',required=True),
                   }
    
    def create_maintenance_sequence(self,cr,uid,ids,context=None):
        for data in self.browse(cr,uid,ids,context=None):
            sops=[]
            for sop in data.sop_ids:
                sops.append((4,sop.id))
            task = False
            
            vals = {
                    'description'       : data.name,
                    'asset_id'          : data.asset_id and data.asset_id.id or False,
                    'maintenance_type'  : data.maintenance_type,
                    'parts_location_id' : data.location_id and data.location_id.id or False,
                    'responsible_id'    : data.responsible_id and data.responsible_id.id or False,
                    'sop_ids'           : sops,
                    'date_planned'      : time.strftime('%Y-%m-%d %H:%M:%S'),
                    'date_scheduled'    : data.first_schedule,
                    'date_execution'    : data.first_schedule,
                    'recurring_id'      : data.id,
                    }
            
            last_mo = self.pool.get('mro.order').search(cr,uid,[('recurring_id','=',data.id)], order='date_scheduled desc',limit=1,context=None)
            
            #===================================================================
            # If Maintenance Order for this Recurring record have generated before, it will continue generating next
            # Maintenance Order schedule. Otherwise, it will start based on First Schedule
            #===================================================================
            if last_mo:
                last_mo = self.pool.get('mro.order').browse(cr,uid,last_mo[0],context=None)
                start   = last_mo.date_scheduled
                start1  = time.strptime(start, '%Y-%m-%d %H:%M:%S')
                start2  = datetime.fromtimestamp(mktime(start1))
                if data.frequency=='daily':
                    foo = start2+relativedelta(days=1)
                elif data.frequency=='weekly':
                    foo = start2+relativedelta(days=7)
                elif data.frequency=='monthly':
                    foo = start2+relativedelta(months=1)
                elif data.frequency=='quarterly':
                    foo = start2+relativedelta(months=3)
                elif data.frequency=='yearly':
                    foo = start2+relativedelta(years=1)
                start = foo.strftime('%Y-%m-%d %H:%M:%S')
            else:
                start   = data.first_schedule
            
            start1  = time.strptime(start, '%Y-%m-%d %H:%M:%S')
            start2  = datetime.fromtimestamp(mktime(start1))
            end     = data.first_schedule
            if data.recurring_period=='days':
                end = start2+relativedelta(days=data.recurring_amount)
            elif data.recurring_period=='weeks':
                end = start2+relativedelta(days=data.recurring_amount*7)
            elif data.recurring_period=='months':
                end = start2+relativedelta(months=data.recurring_amount)
            elif data.recurring_period=='quarters':
                end = start2+relativedelta(months=data.recurring_amount*3)
            elif data.recurring_period=='years':
                end = start2+relativedelta(years=data.recurring_amount)
            end = end.strftime('%Y-%m-%d %H:%M:%S')
            
            foo    = start
            while foo<end:
                #===============================================================
                # Create corresponding Task
                #===============================================================
                if data.maintenance_type in ['cm','pm']:
                    task_vals = {
                    'name'              : data.name,
                    'asset_id'          : data.asset_id and data.asset_id.id or False,
                    'maintenance_type'  : data.maintenance_type,
                    'mo_id'             : False, # this field will be updated once Maintenance Order created "LINK MAINTENANCE ORDER TO TASK"
                    'sop_ids'           : sops
                    }
                    task = self.pool.get('mro.task').create(cr,uid,task_vals,context=None)
                
                #===============================================================
                # Create Maintenance Orders
                #===============================================================
                vals['date_scheduled'] = foo
                vals['date_execution'] = foo
                vals['name'] = self.pool.get('ir.sequence').get(cr, uid, 'mro.order') or '/'
                vals['task_id'] = int(task)
                
                mo = self.pool.get('mro.order').create(cr,uid,vals)
                self.pool.get('mro.task').write(cr,uid,int(task),{'mo_id':int(mo)}) #LINK MAINTENANCE ORDER TO TASK
                
                #===============================================================
                # Get next maintenance date
                #===============================================================
                foo1  = time.strptime(foo, '%Y-%m-%d %H:%M:%S')
                foo2  = datetime.fromtimestamp(mktime(foo1))
                if data.frequency=='daily':
                    foo = foo2+relativedelta(days=1)
                elif data.frequency=='weekly':
                    foo = foo2+relativedelta(days=7)
                elif data.frequency=='monthly':
                    foo = foo2+relativedelta(months=1)
                elif data.frequency=='quarterly':
                    foo = foo2+relativedelta(months=3)
                elif data.frequency=='yearly':
                    foo = foo2+relativedelta(years=1)
                foo = foo.strftime('%Y-%m-%d %H:%M:%S')
        return True
mro_recurring()