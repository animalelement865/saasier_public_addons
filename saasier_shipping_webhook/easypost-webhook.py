#!/usr/bin/env python
import xmlrpclib
from flask import Flask, request, json
import easypost
import logging

username = 'admin'
pwd = 'password'
dbname = 'dbname'

sock_common = xmlrpclib.ServerProxy('http://domain:8069/xmlrpc/common', allow_none=True)
uid = sock_common.login(dbname, username, pwd)
sock = xmlrpclib.ServerProxy('http://domain:8069/xmlrpc/object', allow_none=True)

app = Flask(__name__)

#The List of Status ordred by the less woriest state.
list_of_status = ['delivered','out_for_delivery','in_transit','pre_transit','label-sent','available_for_pickup','done','ship_queue','assigned','confirmed','auto','draft','return_to_sender','failure','unknown','cancel','exception']

@app.route("/easypost", methods = ['POST'])
def api_message():
    if request.headers['Content-Type'] == 'application/json':
        json = request.json
        #logging.info(request.json)

        # This section needs to look for messages where purchasing of the label failed for some reason. 
        # Under result/status in the json if creation_failed or postage_purchase_failed > 0 update 
        # stock_picking_out.state = "Failed" 
        #if json["description"] == 'batch.updated':
        #   if json["result"]["status"] == 'creation_failed'
        
        
        if json["description"] == 'tracker.updated':
            shipping_state = json["result"]["status"]
            logging.info("Tracker Updated")
            stock_move_out_ids = sock.execute(dbname, uid, pwd, 'stock.move', 'search', [('carrier_tracking_ref', '=', json["result"]["tracking_code"])])
            stock_picking_out_ids = sock.execute(dbname, uid, pwd, 'stock.picking.out', 'search', [('move_lines', 'in', stock_move_out_ids)])
            # print stock_picking_out_ids
            # stock_picking_record = sock.execute(dbname, uid, pwd, 'stock.picking.out', 'read', stock_picking_out_ids, ['state','exception_description'])
            if shipping_state in ('return_to_sender','failure','unknown'):
                shipping_status = { 
                    'label_state': 'exception',
                    'exception_description': shipping_state,
                }
                logging.info("Tracking Number " + json["result"]["tracking_code"])
                sock.execute(dbname, uid, pwd, 'stock.move', 'write', stock_move_out_ids, shipping_status)
            elif shipping_state == 'cancelled':
                shipping_status = { 
                            'label_state': 'cancel',
                        }
                logging.info("Tracking Number" + json["result"]["tracking_code"])
                sock.execute(dbname, uid, pwd, 'stock.move', 'write', stock_move_out_ids, shipping_status)
            else:
                if shipping_state=='available_for_pickup':
                    shipping_state='delivered'
                    
                shipping_status = { 
                        'label_state': shipping_state
                    }
                logging.info("Tracking Number" + json["result"]["tracking_code"])
                sock.execute(dbname, uid, pwd, 'stock.move', 'write', stock_move_out_ids, shipping_status)
            
            for picking_record in sock.execute(dbname, uid, pwd, 'stock.picking.out', 'read', stock_picking_out_ids, ['move_lines','state']):
                sock.execute(dbname, uid, pwd, 'stock.picking.out', 'update_label_status', stock_picking_out_ids)
                
                #THIS CAN BE IGNORED, TO AVOID HAVING DUPLICATE CODES, THE FUNCTION TO UPDATE LABELS ALREADY EXISTS IN ORM, THE LINE IN TOP EXECUTES IT.
                # worriest_state = ''
                # for move_id in picking_record['move_lines']:
                    # #Get the Worriest State of Stock.Moves
                    # move_record = sock.execute(dbname, uid, pwd, 'stock.move', 'read', move_id, ['state'])
                    # if worriest_state=='' or list_of_status.index(move_record['state']) > list_of_status.index(worriest_state) :
                        # worriest_state = move_record['state']
                    # #Get the Worriest State of Label_States if it's defined already ( if label is created )
                    # move_record = sock.execute(dbname, uid, pwd, 'stock.move', 'read', move_id, ['label_state'])
                    # if move_record['label_state'] and move_record['label_state']!='':
                        # if worriest_state=='' or list_of_status.index(move_record['label_state']) > list_of_status.index(worriest_state) :
                            # worriest_state = move_record['label_state']
                    # #Now check if there is already the worriest state in delivery order, or put instead of it the new worriest state "worriest_state".
                    # if not (picking_record['state'] in list_of_status) or (list_of_status.index(picking_record['state']) < worriest_state):
                        # if worriest_state=='available_for_pickup':
                            # worriest_state='delivered'
                        # sock.execute(dbname, uid, pwd, 'stock.picking.out', 'write', stock_picking_out_ids, {'state': worriest_state})
                    # elif worriest_state in ('return_to_sender','failure','unknown'):
                        # sock.execute(dbname, uid, pwd, 'stock.picking.out', 'write', stock_picking_out_ids, {'state': 'exception', 'exception_description': worriest_state})
                
                
            
        #print request.data
        #print json.dumps(request.json[description])
        return "200"
    else:
        return "415 Unsupported Media Type"

logging.basicConfig(level=logging.DEBUG, filename="/home/webhook/easypost-webhook.log", filemode="a+", format="%(asctime)-15s %(levelname)-8s %(message)s")
app.run(host='0.0.0.0',debug=True)
