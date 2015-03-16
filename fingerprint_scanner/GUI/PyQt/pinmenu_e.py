# -*- coding: utf-8 -*-

# Form implementation generated from reading ui file 'pinmenu.ui'
#
# Created: Mon Nov  3 15:24:45 2014
#      by: PyQt4 UI code generator 4.10.4
#
# WARNING! All changes made in this file will be lost!

from PyQt4 import QtCore, QtGui
import sys
import ConfigParser
from enroll import Ui_enroll
from threading import Timer


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

class Ui_pinmenu(QtGui.QMainWindow):


    def __init__(self):

        QtGui.QMainWindow.__init__(self)
        self.setupUi(self)

    def setupUi(self, pinmenu):
        pinmenu.setObjectName(_fromUtf8("pinmenu"))
        pinmenu.resize(1024, 600)
        self.centralwidget = QtGui.QWidget(pinmenu)
        self.centralwidget.setObjectName(_fromUtf8("centralwidget"))
        self.return_btn = QtGui.QPushButton(self.centralwidget)
        self.return_btn.setGeometry(QtCore.QRect(10, 10, 131, 51))
        self.return_btn.setObjectName(_fromUtf8("return_btn"))
        self.pinWidget = QtGui.QWidget(self.centralwidget)
        self.pinWidget.setGeometry(QtCore.QRect(290, 80, 391, 481))
        self.pinWidget.setObjectName(_fromUtf8("pinWidget"))
        self.seven_btn = QtGui.QPushButton(self.pinWidget)
        self.seven_btn.setGeometry(QtCore.QRect(10, 120, 111, 71))
        self.seven_btn.setObjectName(_fromUtf8("seven_btn"))
        self.eight_btn = QtGui.QPushButton(self.pinWidget)
        self.eight_btn.setGeometry(QtCore.QRect(140, 120, 111, 71))
        self.eight_btn.setObjectName(_fromUtf8("eight_btn"))
        self.nine_btn = QtGui.QPushButton(self.pinWidget)
        self.nine_btn.setGeometry(QtCore.QRect(270, 120, 111, 71))
        self.nine_btn.setObjectName(_fromUtf8("nine_btn"))
        self.four_btn = QtGui.QPushButton(self.pinWidget)
        self.four_btn.setGeometry(QtCore.QRect(10, 210, 111, 71))
        self.four_btn.setObjectName(_fromUtf8("four_btn"))
        self.five_btn = QtGui.QPushButton(self.pinWidget)
        self.five_btn.setGeometry(QtCore.QRect(140, 210, 111, 71))
        self.five_btn.setObjectName(_fromUtf8("five_btn"))
        self.six_btn = QtGui.QPushButton(self.pinWidget)
        self.six_btn.setGeometry(QtCore.QRect(270, 210, 111, 71))
        self.six_btn.setObjectName(_fromUtf8("six_btn"))
        self.one_btn = QtGui.QPushButton(self.pinWidget)
        self.one_btn.setGeometry(QtCore.QRect(10, 300, 111, 71))
        self.one_btn.setObjectName(_fromUtf8("one_btn"))
        self.zero_btn = QtGui.QPushButton(self.pinWidget)
        self.zero_btn.setGeometry(QtCore.QRect(10, 390, 111, 71))
        self.zero_btn.setObjectName(_fromUtf8("zero_btn"))
        self.three_btn = QtGui.QPushButton(self.pinWidget)
        self.three_btn.setGeometry(QtCore.QRect(270, 300, 111, 71))
        self.three_btn.setObjectName(_fromUtf8("three_btn"))
        self.two_btn = QtGui.QPushButton(self.pinWidget)
        self.two_btn.setGeometry(QtCore.QRect(140, 300, 111, 71))
        self.two_btn.setObjectName(_fromUtf8("two_btn"))
        self.ok_btn = QtGui.QPushButton(self.pinWidget)
        self.ok_btn.setGeometry(QtCore.QRect(270, 390, 111, 71))
        self.ok_btn.setObjectName(_fromUtf8("ok_btn"))
        self.pinEdit = QtGui.QLineEdit(self.pinWidget)
        self.pinEdit.setGeometry(QtCore.QRect(70, 50, 251, 51))
        font = QtGui.QFont()
        font.setPointSize(35)
        self.pinEdit.setFont(font)
        self.pinEdit.setAlignment(QtCore.Qt.AlignCenter)
        self.pinEdit.setObjectName(_fromUtf8("pinEdit"))
        self.back_btn = QtGui.QPushButton(self.pinWidget)
        self.back_btn.setGeometry(QtCore.QRect(140, 390, 111, 71))
        self.back_btn.setObjectName(_fromUtf8("back_btn"))


        self.wrongPin = QtGui.QLabel(self.pinWidget)
        self.wrongPin.setGeometry(QtCore.QRect(70, 50, 251, 51))
        font = QtGui.QFont()
        font.setPointSize(20)
        self.wrongPin.setFont(font)
        self.wrongPin.setAlignment(QtCore.Qt.AlignCenter)

        self.wrongPin.setObjectName(_fromUtf8("wrongPin"))

        self.wrongPin.setVisible(False)
        palette = QtGui.QPalette()


        palette.setColor(QtGui.QPalette.Foreground,QtCore.Qt.red)
        self.wrongPin.setPalette(palette)

        pinmenu.setCentralWidget(self.centralwidget)

        self.retranslateUi(pinmenu)
        QtCore.QMetaObject.connectSlotsByName(pinmenu)

    def retranslateUi(self, pinmenu):

        pinmenu.setWindowTitle(_translate("pinmenu", "MainWindow", None))
        self.return_btn.setText(_translate("pinmenu", "Return", None))
        self.seven_btn.setText(_translate("pinmenu", "7", None))
        self.eight_btn.setText(_translate("pinmenu", "8", None))
        self.nine_btn.setText(_translate("pinmenu", "9", None))
        self.four_btn.setText(_translate("pinmenu", "4", None))
        self.five_btn.setText(_translate("pinmenu", "5", None))
        self.six_btn.setText(_translate("pinmenu", "6", None))
        self.one_btn.setText(_translate("pinmenu", "1", None))
        self.zero_btn.setText(_translate("pinmenu", "0", None))
        self.three_btn.setText(_translate("pinmenu", "3", None))
        self.two_btn.setText(_translate("pinmenu", "2", None))
        self.ok_btn.setText(_translate("pinmenu", "OK", None))
        self.pinEdit.setText(_translate("pinmenu", "", None))
        self.pinEdit.setMaxLength(4)
        self.wrongPin.setText(_translate("pinmenu", "Wrong PIN", None))
        self.back_btn.setText(_translate("pinmenu", "back", None))
        self.centralwidget.setStyleSheet("""
    	.QWidget#centralwidget {
    	background-image: url(res/images/background.jpg);
    	}""")
        self.pinWidget.setStyleSheet("""

        .QWidget#pinWidget {
            border-width: 3px;
            border-color: black;
    	    background-color: white;
	        border-radius: 10px;
    	}""")

        self.pinEdit.setFocus()

        self.btn_ok_f='btn_ok'
        self.btn_back_f='btn_back'
        self.btn_one_f='btn_one'
        self.btn_two_f='btn_two'
        self.btn_three_f='btn_three'
        self.btn_four_f='btn_four'
        self.btn_five_f='btn_five'
        self.btn_six_f='btn_six'
        self.btn_seven_f='btn_seven'
        self.btn_eight_f='btn_eight'
        self.btn_nine_f='btn_nine'
        self.btn_zero_f='btn_zero'

        self.funcs = [
        self.btn_ok_f,
        self.btn_back_f,
        self.btn_one_f,
        self.btn_two_f,
        self.btn_three_f,
        self.btn_four_f,
        self.btn_five_f,
        self.btn_six_f,
        self.btn_seven_f,
        self.btn_eight_f,
        self.btn_nine_f,
        self.btn_zero_f
                         ]

        self.buttons = [
            self.ok_btn,
            self.back_btn,
            self.one_btn,
            self.two_btn,
            self.three_btn,
            self.four_btn,
            self.five_btn,
            self.six_btn,
            self.seven_btn,
            self.eight_btn,
            self.nine_btn,
            self.zero_btn


        ]

        self.pin = ""
        def pineEdit_clear():
            line_temp = QtGui.QApplication.focusWidget()
            if ( type(line_temp) == QtGui.QLineEdit):
                 line_temp.setText('')


        def btn_one():
            line_temp = QtGui.QApplication.focusWidget()
            if ( type(line_temp) == QtGui.QLineEdit and len(line_temp.text()) < 4):
                 line_temp.setText(line_temp.text()+'*')
                 self.pin = self.pin + '1'

        def btn_two():
            line_temp = QtGui.QApplication.focusWidget()
            if ( type(line_temp) == QtGui.QLineEdit and len(line_temp.text()) < 4):
                 line_temp.setText(line_temp.text()+'*')
                 self.pin = self.pin + '2'

        def btn_three():
            line_temp = QtGui.QApplication.focusWidget()
            if ( type(line_temp) == QtGui.QLineEdit and len(line_temp.text()) < 4):
                 line_temp.setText(line_temp.text()+'*')
                 self.pin = self.pin + '3'

        def btn_four():
            line_temp = QtGui.QApplication.focusWidget()
            if ( type(line_temp) == QtGui.QLineEdit and len(line_temp.text()) < 4):
                 line_temp.setText(line_temp.text()+'*')
                 self.pin = self.pin + '4'

        def btn_five():
            line_temp = QtGui.QApplication.focusWidget()
            if ( type(line_temp) == QtGui.QLineEdit and len(line_temp.text()) < 4):
                 line_temp.setText(line_temp.text()+'*')
                 self.pin = self.pin + '5'

        def btn_six():
            line_temp = QtGui.QApplication.focusWidget()
            if ( type(line_temp) == QtGui.QLineEdit and len(line_temp.text()) < 4):
                 line_temp.setText(line_temp.text()+'*')
                 self.pin = self.pin + '6'

        def btn_seven():
            line_temp = QtGui.QApplication.focusWidget()
            if ( type(line_temp) == QtGui.QLineEdit and len(line_temp.text()) < 4):
                 line_temp.setText(line_temp.text()+'*')
                 self.pin = self.pin + '7'

        def btn_eight():
            line_temp = QtGui.QApplication.focusWidget()
            if ( type(line_temp) == QtGui.QLineEdit and len(line_temp.text()) < 4):
                 line_temp.setText(line_temp.text()+'*')
                 self.pin = self.pin + '8'

        def btn_nine():
            line_temp = QtGui.QApplication.focusWidget()
            if ( type(line_temp) == QtGui.QLineEdit and len(line_temp.text()) < 4):
                 line_temp.setText(line_temp.text()+'*')
                 self.pin = self.pin + '9'

        def btn_zero():
            line_temp = QtGui.QApplication.focusWidget()
            if ( type(line_temp) == QtGui.QLineEdit and len(line_temp.text()) < 4):
                 line_temp.setText(line_temp.text()+'*')
                 self.pin = self.pin + '0'

        def btn_ok():
            # Here should be inserted the code to be executed when the pin is typed in
            config = ConfigParser.ConfigParser()
            config.readfp(open('config.ini'))
            pin = str(config.getint('Login','password'))

            if  pin == self.pin :

               self.enr = Ui_enroll()
               self.enr.show()
               #To launch the application in full screen mode, Comment the previous line and Uncomment the next one.
               #self.enr.showFullScreen()

               self.close()
            else:

               self.wrongPin.setVisible(True)

               t = Timer(1,hide_message)
               t.start()


               self.pinEdit.setText("")



               self.pin = ""

            return None

        def hide_message():
            self.wrongPin.setVisible(False)

        def btn_back():
            line_temp = QtGui.QApplication.focusWidget()
            if ( type(line_temp) == QtGui.QLineEdit):
                 line_temp.setText(line_temp.text()[:-1])
                 self.pin = self.pin[:-1]


        def btn_return():
            self.close()

        for button in self.buttons:
            button.setFocusPolicy(QtCore.Qt.NoFocus)


        for (button,func) in zip(self.buttons,self.funcs):

            button.clicked.connect(locals()[func])
            button.setStyleSheet("""

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
        self.return_btn.setStyleSheet("""

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
        self.return_btn.clicked.connect(btn_return)


if __name__ == '__main__':
    app = QtGui.QApplication(sys.argv)
    ex = Ui_pinmenu()
    ex.show()
    #To launch the application in full screen mode, Comment the previous line and Uncomment the next one.
    #ex.showFullScreen()

    sys.exit(app.exec_())