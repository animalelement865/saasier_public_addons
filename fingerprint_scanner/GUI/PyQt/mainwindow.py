# -*- coding: utf-8 -*-

# Form implementation generated from reading ui file 'mainwindow.ui'
#
# Created: Mon Nov  3 14:22:02 2014
#      by: PyQt4 UI code generator 4.10.4
#
# WARNING! All changes made in this file will be lost!

from PyQt4 import QtCore, QtGui
import sys
import pinmenu_s
import pinmenu_e
import ConfigParser

try:
    _fromUtf8 = QtCore.QString.fromUtf8
except AttributeError:
    def _fromUtf8(s):
        return s

try:
    _encoding = QtGui.QApplication.UnicodeUTF8
    def _translate(context, text, disambig):
        return QtGui.QApplication.translate(context, text, disambig, _encoding)
except AttributeError:
    def _translate(context, text, disambig):
        return QtGui.QApplication.translate(context, text, disambig)

class Ui_MainWindow(QtGui.QMainWindow):


    def __init__(self):

        QtGui.QMainWindow.__init__(self)
        self.setupUi(self)


    def setupUi(self, MainWindow):
        MainWindow.setObjectName(_fromUtf8("MainWindow"))
        MainWindow.resize(1024, 600)

        self.centralWidget = QtGui.QWidget(MainWindow)

        # Setting the background.
        self.centralWidget.setStyleSheet("""
	    .QWidget {
	        background-image: url(res/images/background.jpg);
	    }
	    """)

        self.centralWidget.setObjectName(_fromUtf8("centralWidget"))
        self.enroll_btn = QtGui.QPushButton(self.centralWidget)
        self.enroll_btn.setGeometry(QtCore.QRect(250, 370, 131, 51))
        self.enroll_btn.setObjectName(_fromUtf8("enroll_btn"))
        self.settings_btn = QtGui.QPushButton(self.centralWidget)
        self.settings_btn.setGeometry(QtCore.QRect(643, 370, 131, 51))
        self.settings_btn.setObjectName(_fromUtf8("settings_btn"))



        self.mainLabel = QtGui.QLabel(self.centralWidget)
        self.mainLabel.setGeometry(QtCore.QRect(160, 170, 681, 51))
        font = QtGui.QFont()
        font.setPointSize(27)
        self.mainLabel.setFont(font)
        self.mainLabel.setAlignment(QtCore.Qt.AlignCenter)
        self.mainLabel.setObjectName(_fromUtf8("mainLabel"))
        palette = QtGui.QPalette()
        palette.setColor(QtGui.QPalette.Foreground,QtCore.Qt.white)
        self.mainLabel.setPalette(palette)

        MainWindow.setCentralWidget(self.centralWidget)

        self.retranslateUi(MainWindow)
        QtCore.QMetaObject.connectSlotsByName(MainWindow)
        self.enroll_btn.clicked.connect(self.enroll_menu)
        self.settings_btn.clicked.connect(self.settings_menu)



        # setting the Style sheets of the buttons.
        self.settings_btn.setStyleSheet("""

        QPushButton {

        background-color: white;
        border-style: outset;
        border-width: 1px;
        border-color: black;
        font: bold 14px;
        border-radius: 10px;


        }

        QPushButton:pressed {
        background-color: grey;
        border-style: inset;
        }

        """)

        self.enroll_btn.setStyleSheet("""

        QPushButton {

        background-color: white;
        border-style: outset;
        border-width: 1px;
        border-color: black;
        font: bold 14px;
        border-radius: 10px;


        }

        QPushButton:pressed {
        background-color: grey;
        border-style: inset;
        }

        """)



    def retranslateUi(self, MainWindow):
        MainWindow.setWindowTitle(_translate("MainWindow", "MainWindow", None))
        self.enroll_btn.setText(_translate("MainWindow", "Enroll", None))
        self.settings_btn.setText(_translate("MainWindow", "Settings", None))
        self.mainLabel.setText(_translate("MainWindow", "Waiting for a valid finger...", None))
        self.mainLabel.setVisible(False)

        # This code loads the settings from the settings.ini File.
        config = ConfigParser.ConfigParser()
        config.readfp(open('settings.ini'))
        username = str(config.get('settings','username'))
        database = str(config.get('settings','database'))
        server = str(config.get('settings','server'))
        password = str(config.get('settings','password'))
        last_id = str(config.getint('settings','last_id'))
        print " Username Loaded : " + username
        print " Password Loaded : " + password
        print " Server Loaded : " + server
        print " Database Loaded : " + database
        print " Last ID Loaded : " + last_id
        print " Constructed sock_common variable : " + server + "/xmlrpc/common"
        print " Constructed sock variable : " + server + "/xmlrpc/object"

        # Main code Reading the finger prints would probably Go here :

        # For instance in the fingerprint algorithm if you want to show a Text label on the
        # Screen you would go about it this way :


        #   self.mainLabel.setText("Waiting for a valid finger...")
        #   self.mainLabel.setVisible(True)
        #
        # before each text you put on the screen you have to set Its color first :
        #   palette = QtGui.QPalette()
        #   palette.setColor(QtGui.QPalette.Foreground, QtCore.Qt.white)
        #   self.mainLabel.setPalette(palette)
        # and you just replace white with red if you want the color to be Red in case of an Alert.



    # I used this function as a Serial simulator.
    def serial_simulator(self, serial):

        palette = QtGui.QPalette()
        palette.setColor(QtGui.QPalette.Foreground, QtCore.Qt.white)
        self.mainLabel.setPalette(palette)

        if (serial == 'm,Waiting for valid finger to enroll' or serial == '.'):
            self.mainLabel.setText("Waiting for a valid finger...")
            self.mainLabel.setVisible(True)
        elif ('m,Image taken' in serial ):
            self.mainLabel.setText("Image taken.")
        elif (serial == 'm,image converted'):
            self.mainLabel.setText("Image converted.")
        elif (serial == 'm,Remove finger'):
            self.mainLabel.setText("Remove finger.")
        elif (serial == 'm,Place same finger again'):
            self.mainLabel.setText("Place same finger again.")
        elif (serial == 'm,Prints matched!'):
            self.mainLabel.setText("Prints matched!")
        elif ( serial == 'm,Stored!'):
            self.mainLabel.setText("Fingerprints stored !")
        elif (serial == 'm,NoSensor'):
            palette = QtGui.QPalette()
            palette.setColor(QtGui.QPalette.Foreground, QtCore.Qt.red)

            self.mainLabel.setPalette(palette)
            self.mainLabel.setText("The Fingerprint sensor was not found.")
            self.mainLabel.setVisible(True)
        else:
            self.mainLabel.setText(serial)





    # This is the method triggered when clicking the Enroll button.
    def enroll_menu(self):
        self.enr = pinmenu_e.Ui_pinmenu()
        self.enr.show()
        #To launch the application in full screen mode, Comment the previous line and Uncomment the next one.
        #self.enr.showFullScreen()


    # This is the button triggered when clicking the Settings button
    def settings_menu(self):
        self.sett = pinmenu_s.Ui_pinmenu()

        self.sett.show()
        #To launch the application in full screen mode, Comment the previous line and Uncomment the next one.
        #self.sett.showFullScreen()




# The main function.
if __name__ == '__main__':
    app = QtGui.QApplication(sys.argv)
    ex = Ui_MainWindow()
    ex.show()
    #To launch the application in full screen mode, Comment the previous line and Uncomment the next one.
    #ex.showFullScreen()

    sys.exit(app.exec_())