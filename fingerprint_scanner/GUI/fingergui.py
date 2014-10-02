from datetime import datetime
import xmlrpclib
import serial
from Tkinter import *
from PIL import ImageTk, Image

path = 'background.jpg'

root = Tk()

employee_name_var = StringVar()

#w, h = root.winfo_screenwidth(), root.winfo_screenheight()
#root.overrideredirect(1)
#root.geometry("%dx%d+0+0" % (w, h))

#root.focus_set()
#root.bind("<Escape>", lambda e: e.widget.quit())

canvas = Canvas(root, width=1024, height=600)
canvas.pack(expand = YES, fill = BOTH)

img = ImageTk.PhotoImage(Image.open(path))
canvas.create_image(0, 0, image = img, anchor = NW)

employee_text = canvas.create_text(600, 200, font="Arial 40 bold", fill="white")
canvas.itemconfig(employee_text, text="Please Sign In")

employee_status_text = canvas.create_text(600, 300, font="Arial 20 bold", fill="white")
canvas.itemconfig(employee_status_text, text="")

#canvas.insert(employee_text, 12, "new ")

#canvas_id = canvas.create_text(500, 50, fill="#476042" anchor="nw")
#canvas.itemconfig(canvas_id, text="words and stuff")

#canvas.insert(canvas_id, 12, "new ")


#panel = Label(root, image = img)

#app = Frame(root)
#app.grid()

#panel.pack(side = "bottom", fill = "both", expand = "yes")

def button1_function():
        print "button1_function"

B1 = Button(root, text="Settings", command = button1_function)
B1.pack()

B2 = Button(root, text="Enroll")
B2.pack()

label = Label(root, textvariable = employee_name_var, relief=RAISED)
employee_name_var.set("Welcome")
label.pack()

ser = serial.Serial('/dev/ttyACM0', 9600)



username = 'admin'
pwd = 'password'
dbname = 'database'

sock_common = xmlrpclib.ServerProxy ('http://url.com/xmlrpc/common', allow_none=True)

uid = sock_common.login(dbname, username, pwd)
sock = xmlrpclib.ServerProxy('http://url.com/xmlrpc/object', allow_none=True)

message1 = ser.readline()
formatted_message1 = message1.strip().split(",")
print formatted_message1

#if formatted_message1[0] == 'm' and formatted_message1[1] == 'Sensor':
#	sensor_status = 1
#	print sensor_status
#else: 
#	sensor_status = 2
#	if ser.isOpen():
#		ser.close()
#		ser.open()
#	print sensor_status

#if formatted_message1[0] == 'm' and formatted_message1[1] == 'Waiting for valid finger':
#	sensor_status = 3
#	print sensor_status
#else: 	
#	sensor_status = 4
#	print sensor_status
#	if ser.isOpen():
#		ser.close()
#		ser.open()

#user_input = raw_input("")

while True:

#	user_input = raw_input("")
#	print user_input
	#root.mainloop()
	message = ser.readline()
	#print(message)
	formatted_message =  message.strip().split(",")
	print formatted_message

	if True:
	#if formatted_message[0] == 'm' and formatted_message[1] == 'Sensor':

		if formatted_message[0] == 'i':

			print formatted_message[1]
			print("User Identified")

			fields = ['id','name']
			employee_ids = sock.execute(dbname, uid, pwd, 'hr.employee', 'search', [('otherid','=', formatted_message[1])])
			employee_data = sock.execute(dbname, uid, pwd, 'hr.employee', 'read', employee_ids, fields)
			dt = datetime.now()
			current_time = str(dt)
			current_time = dt.strftime('%m/%d/%Y %H:%M:%S')
			print employee_data[0]['id']
		
				
			time_sheet_fields = ['action']
			time_sheet_ids = sock.execute(dbname, uid, pwd, 'hr.attendance', 'search', [('employee_id', '=', employee_data[0]['id'])])

			canvas.itemconfig(employee_text, text=employee_data[0]['name'])
			#employee_name_var.set(employee_data[0]['name'])
			#label.pack()

			if not time_sheet_ids:
				print 'no attendances yet'
				time_sheet_values = { 'employee_id': employee_data[0]['id'],
        				              'name': current_time,
                		       		      'action': 'sign_in'
		        	            	    }
				time_sheet = sock.execute(dbname, uid, pwd, 'hr.attendance', 'create', time_sheet_values)


			else:
				print 'there are attendances'
				time_sheet_data = sock.execute(dbname, uid, pwd, 'hr.attendance', 'read', time_sheet_ids[0], time_sheet_fields)		

				print time_sheet_data['action']

				if time_sheet_data['action'] == 'sign_in':
					current_action = 'sign_out'
					canvas.itemconfig(employee_status_text, text="Signed Out")

				if time_sheet_data['action'] == 'sign_out':
					current_action = 'sign_in'
					canvas.itemconfig(employee_status_text, text="Signed In")


				time_sheet_values = { 'employee_id': employee_data[0]['id'],
						      'name': current_time,
						      'action': current_action
				  			}

				print time_sheet_values

				time_sheet = sock.execute(dbname, uid, pwd, 'hr.attendance', 'create', time_sheet_values)


		root.update()
