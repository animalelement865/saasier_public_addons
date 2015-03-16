# -*- coding: utf-8 -*-

# Form implementation generated from reading ui file 'enroll.ui'
#
# Created: Thu Nov  6 13:44:54 2014
#      by: PyQt4 UI code generator 4.10.4
#
# WARNING! All changes made in this file will be lost!

from PyQt4 import QtCore, QtGui

import sys
import string
from PyQt4.QtGui import QStandardItem, QStandardItemModel

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

class Ui_enroll(QtGui.QMainWindow):

    def __init__(self):

        QtGui.QMainWindow.__init__(self)
        self.setupUi(self)


    def setupUi(self, enroll):
        enroll.setObjectName(_fromUtf8("enroll"))
        enroll.resize(1024, 600)
        self.centralwidget = QtGui.QWidget(enroll)
        self.centralwidget.setObjectName(_fromUtf8("centralwidget"))
        self.m_btn = QtGui.QPushButton(self.centralwidget)
        self.m_btn.setGeometry(QtCore.QRect(550, 490, 71, 51))
        self.m_btn.setObjectName(_fromUtf8("m_btn"))
        self.a_btn = QtGui.QPushButton(self.centralwidget)
        self.a_btn.setGeometry(QtCore.QRect(10, 430, 71, 51))
        self.a_btn.setObjectName(_fromUtf8("a_btn"))
        self.d_btn = QtGui.QPushButton(self.centralwidget)
        self.d_btn.setGeometry(QtCore.QRect(190, 430, 71, 51))
        self.d_btn.setObjectName(_fromUtf8("d_btn"))
        self.f_btn = QtGui.QPushButton(self.centralwidget)
        self.f_btn.setGeometry(QtCore.QRect(280, 430, 71, 51))
        self.f_btn.setObjectName(_fromUtf8("f_btn"))
        self.slash_btn = QtGui.QPushButton(self.centralwidget)
        self.slash_btn.setGeometry(QtCore.QRect(730, 490, 71, 51))
        self.slash_btn.setObjectName(_fromUtf8("slash_btn"))
        self.at_btn = QtGui.QPushButton(self.centralwidget)
        self.at_btn.setGeometry(QtCore.QRect(910, 550, 101, 41))
        self.at_btn.setObjectName(_fromUtf8("at_btn"))
        self.w_btn = QtGui.QPushButton(self.centralwidget)
        self.w_btn.setGeometry(QtCore.QRect(100, 370, 71, 51))
        self.w_btn.setObjectName(_fromUtf8("w_btn"))
        self.zero_btn = QtGui.QPushButton(self.centralwidget)
        self.zero_btn.setGeometry(QtCore.QRect(820, 310, 71, 51))
        self.zero_btn.setObjectName(_fromUtf8("zero_btn"))
        self.u_btn = QtGui.QPushButton(self.centralwidget)
        self.u_btn.setGeometry(QtCore.QRect(550, 370, 71, 51))
        self.u_btn.setObjectName(_fromUtf8("u_btn"))
        self.v_btn = QtGui.QPushButton(self.centralwidget)
        self.v_btn.setGeometry(QtCore.QRect(280, 490, 71, 51))
        self.v_btn.setObjectName(_fromUtf8("v_btn"))
        self.interro_btn = QtGui.QPushButton(self.centralwidget)
        self.interro_btn.setGeometry(QtCore.QRect(820, 430, 71, 51))
        self.interro_btn.setObjectName(_fromUtf8("interro_btn"))
        self.underscore_btn = QtGui.QPushButton(self.centralwidget)
        self.underscore_btn.setGeometry(QtCore.QRect(790, 550, 101, 41))
        self.underscore_btn.setObjectName(_fromUtf8("underscore_btn"))
        self.r_btn = QtGui.QPushButton(self.centralwidget)
        self.r_btn.setGeometry(QtCore.QRect(280, 370, 71, 51))
        self.r_btn.setObjectName(_fromUtf8("r_btn"))
        self.l_btn = QtGui.QPushButton(self.centralwidget)
        self.l_btn.setGeometry(QtCore.QRect(730, 430, 71, 51))
        self.l_btn.setObjectName(_fromUtf8("l_btn"))
        self.s_btn = QtGui.QPushButton(self.centralwidget)
        self.s_btn.setGeometry(QtCore.QRect(100, 430, 71, 51))
        self.s_btn.setObjectName(_fromUtf8("s_btn"))
        self.z_btn = QtGui.QPushButton(self.centralwidget)
        self.z_btn.setGeometry(QtCore.QRect(10, 490, 71, 51))
        self.z_btn.setObjectName(_fromUtf8("z_btn"))
        self.colon_btn = QtGui.QPushButton(self.centralwidget)
        self.colon_btn.setGeometry(QtCore.QRect(820, 490, 71, 51))
        self.colon_btn.setObjectName(_fromUtf8("colon_btn"))
        self.three_btn = QtGui.QPushButton(self.centralwidget)
        self.three_btn.setGeometry(QtCore.QRect(190, 310, 71, 51))
        self.three_btn.setObjectName(_fromUtf8("three_btn"))
        self.back_btn = QtGui.QPushButton(self.centralwidget)
        self.back_btn.setGeometry(QtCore.QRect(910, 310, 101, 51))
        self.back_btn.setObjectName(_fromUtf8("back_btn"))
        self.p_btn = QtGui.QPushButton(self.centralwidget)
        self.p_btn.setGeometry(QtCore.QRect(820, 370, 71, 51))
        self.p_btn.setObjectName(_fromUtf8("p_btn"))
        self.seven_btn = QtGui.QPushButton(self.centralwidget)
        self.seven_btn.setGeometry(QtCore.QRect(550, 310, 71, 51))
        self.seven_btn.setObjectName(_fromUtf8("seven_btn"))
        self.userlist = QtGui.QListWidget(self.centralwidget)
        self.userlist.setGeometry(QtCore.QRect(230, 110, 461, 181))
        self.userlist.setIconSize(QtCore.QSize(10, 12))
        self.userlist.setObjectName(_fromUtf8("userlist"))
        self.b_btn = QtGui.QPushButton(self.centralwidget)
        self.b_btn.setGeometry(QtCore.QRect(370, 490, 71, 51))
        self.b_btn.setObjectName(_fromUtf8("b_btn"))
        self.y_btn = QtGui.QPushButton(self.centralwidget)
        self.y_btn.setGeometry(QtCore.QRect(460, 370, 71, 51))
        self.y_btn.setObjectName(_fromUtf8("y_btn"))
        self.t_btn = QtGui.QPushButton(self.centralwidget)
        self.t_btn.setGeometry(QtCore.QRect(370, 370, 71, 51))
        self.t_btn.setObjectName(_fromUtf8("t_btn"))
        self.e_btn = QtGui.QPushButton(self.centralwidget)
        self.e_btn.setGeometry(QtCore.QRect(190, 370, 71, 51))
        self.e_btn.setObjectName(_fromUtf8("e_btn"))
        self.one_btn = QtGui.QPushButton(self.centralwidget)
        self.one_btn.setGeometry(QtCore.QRect(10, 310, 71, 51))
        self.one_btn.setObjectName(_fromUtf8("one_btn"))
        self.h_btn = QtGui.QPushButton(self.centralwidget)
        self.h_btn.setGeometry(QtCore.QRect(460, 430, 71, 51))
        self.h_btn.setObjectName(_fromUtf8("h_btn"))
        self.usernameedit = QtGui.QLineEdit(self.centralwidget)
        self.usernameedit.setGeometry(QtCore.QRect(230, 40, 311, 51))
        self.usernameedit.setObjectName(_fromUtf8("usernameedit"))
        font = QtGui.QFont()
        font.setPointSize(24)
        self.usernameedit.setFont(font)
        self.search_button = QtGui.QPushButton(self.centralwidget)
        self.search_button.setGeometry(QtCore.QRect(560, 40, 131, 51))
        self.search_button.setObjectName(_fromUtf8("search_button"))
        self.enter_btn = QtGui.QPushButton(self.centralwidget)
        self.enter_btn.setGeometry(QtCore.QRect(910, 370, 101, 111))
        self.enter_btn.setObjectName(_fromUtf8("enter_btn"))
        self.k_btn = QtGui.QPushButton(self.centralwidget)
        self.k_btn.setGeometry(QtCore.QRect(640, 430, 71, 51))
        self.k_btn.setObjectName(_fromUtf8("k_btn"))
        self.q_btn = QtGui.QPushButton(self.centralwidget)
        self.q_btn.setGeometry(QtCore.QRect(10, 370, 71, 51))
        self.q_btn.setObjectName(_fromUtf8("q_btn"))
        self.shift_btn = QtGui.QPushButton(self.centralwidget)
        self.shift_btn.setGeometry(QtCore.QRect(10, 550, 131, 41))
        self.shift_btn.setObjectName(_fromUtf8("shift_btn"))
        self.nine_btn = QtGui.QPushButton(self.centralwidget)
        self.nine_btn.setGeometry(QtCore.QRect(730, 310, 71, 51))
        self.nine_btn.setObjectName(_fromUtf8("nine_btn"))
        self.n_btn = QtGui.QPushButton(self.centralwidget)
        self.n_btn.setGeometry(QtCore.QRect(460, 490, 71, 51))
        self.n_btn.setObjectName(_fromUtf8("n_btn"))
        self.point_btn = QtGui.QPushButton(self.centralwidget)
        self.point_btn.setGeometry(QtCore.QRect(640, 490, 71, 51))
        self.point_btn.setObjectName(_fromUtf8("point_btn"))
        self.four_btn = QtGui.QPushButton(self.centralwidget)
        self.four_btn.setGeometry(QtCore.QRect(280, 310, 71, 51))
        self.four_btn.setObjectName(_fromUtf8("four_btn"))
        self.x_btn = QtGui.QPushButton(self.centralwidget)
        self.x_btn.setGeometry(QtCore.QRect(100, 490, 71, 51))
        self.x_btn.setObjectName(_fromUtf8("x_btn"))
        self.o_btn = QtGui.QPushButton(self.centralwidget)
        self.o_btn.setGeometry(QtCore.QRect(730, 370, 71, 51))
        self.o_btn.setObjectName(_fromUtf8("o_btn"))
        self.hyphen_btn = QtGui.QPushButton(self.centralwidget)
        self.hyphen_btn.setGeometry(QtCore.QRect(910, 490, 101, 51))
        self.hyphen_btn.setObjectName(_fromUtf8("hyphen_btn"))
        self.i_btn = QtGui.QPushButton(self.centralwidget)
        self.i_btn.setGeometry(QtCore.QRect(640, 370, 71, 51))
        self.i_btn.setObjectName(_fromUtf8("i_btn"))
        self.j_btn = QtGui.QPushButton(self.centralwidget)
        self.j_btn.setGeometry(QtCore.QRect(550, 430, 71, 51))
        self.j_btn.setObjectName(_fromUtf8("j_btn"))
        self.five_btn = QtGui.QPushButton(self.centralwidget)
        self.five_btn.setGeometry(QtCore.QRect(370, 310, 71, 51))
        self.five_btn.setObjectName(_fromUtf8("five_btn"))
        self.return_btn = QtGui.QPushButton(self.centralwidget)
        self.return_btn.setGeometry(QtCore.QRect(10, 10, 131, 51))
        self.return_btn.setObjectName(_fromUtf8("pushButton"))
        self.two_btn = QtGui.QPushButton(self.centralwidget)
        self.two_btn.setGeometry(QtCore.QRect(100, 310, 71, 51))
        self.two_btn.setObjectName(_fromUtf8("two_btn"))
        self.six_btn = QtGui.QPushButton(self.centralwidget)
        self.six_btn.setGeometry(QtCore.QRect(460, 310, 71, 51))
        self.six_btn.setObjectName(_fromUtf8("six_btn"))
        self.g_btn = QtGui.QPushButton(self.centralwidget)
        self.g_btn.setGeometry(QtCore.QRect(370, 430, 71, 51))
        self.g_btn.setObjectName(_fromUtf8("g_btn"))
        self.c_btn = QtGui.QPushButton(self.centralwidget)
        self.c_btn.setGeometry(QtCore.QRect(190, 490, 71, 51))
        self.c_btn.setObjectName(_fromUtf8("c_btn"))
        self.eight_btn = QtGui.QPushButton(self.centralwidget)
        self.eight_btn.setGeometry(QtCore.QRect(640, 310, 71, 51))
        self.eight_btn.setObjectName(_fromUtf8("eight_btn"))
        self.space_btn = QtGui.QPushButton(self.centralwidget)
        self.space_btn.setGeometry(QtCore.QRect(160, 550, 611, 41))
        self.space_btn.setObjectName(_fromUtf8("space_btn"))
        enroll.setCentralWidget(self.centralwidget)

        self.retranslateUi(enroll)
        QtCore.QMetaObject.connectSlotsByName(enroll)

    def retranslateUi(self, enroll):
        enroll.setWindowTitle(_translate("enroll", "MainWindow", None))
        self.m_btn.setText(_translate("enroll", "m", None))
        self.a_btn.setText(_translate("enroll", "a", None))
        self.d_btn.setText(_translate("enroll", "d", None))
        self.f_btn.setText(_translate("enroll", "f", None))
        self.slash_btn.setText(_translate("enroll", "/", None))
        self.at_btn.setText(_translate("enroll", "@", None))
        self.w_btn.setText(_translate("enroll", "w", None))
        self.zero_btn.setText(_translate("enroll", "0", None))
        self.u_btn.setText(_translate("enroll", "u", None))
        self.v_btn.setText(_translate("enroll", "v", None))
        self.interro_btn.setText(_translate("enroll", "?", None))
        self.underscore_btn.setText(_translate("enroll", "_", None))
        self.r_btn.setText(_translate("enroll", "r", None))
        self.l_btn.setText(_translate("enroll", "l", None))
        self.s_btn.setText(_translate("enroll", "s", None))
        self.z_btn.setText(_translate("enroll", "z", None))
        self.colon_btn.setText(_translate("enroll", ":", None))
        self.three_btn.setText(_translate("enroll", "3", None))
        self.back_btn.setText(_translate("enroll", "Backwards", None))
        self.p_btn.setText(_translate("enroll", "p", None))
        self.seven_btn.setText(_translate("enroll", "7", None))
        self.b_btn.setText(_translate("enroll", "b", None))
        self.y_btn.setText(_translate("enroll", "y", None))
        self.t_btn.setText(_translate("enroll", "t", None))
        self.e_btn.setText(_translate("enroll", "e", None))
        self.one_btn.setText(_translate("enroll", "1", None))
        self.h_btn.setText(_translate("enroll", "h", None))
        self.search_button.setText(_translate("enroll", "Search", None))
        self.enter_btn.setText(_translate("enroll", "ENTER", None))
        self.k_btn.setText(_translate("enroll", "k", None))
        self.q_btn.setText(_translate("enroll", "q", None))
        self.shift_btn.setText(_translate("enroll", "Shift", None))
        self.nine_btn.setText(_translate("enroll", "9", None))
        self.n_btn.setText(_translate("enroll", "n", None))
        self.point_btn.setText(_translate("enroll", ".", None))
        self.four_btn.setText(_translate("enroll", "4", None))
        self.x_btn.setText(_translate("enroll", "x", None))
        self.o_btn.setText(_translate("enroll", "o", None))
        self.hyphen_btn.setText(_translate("enroll", "-", None))
        self.i_btn.setText(_translate("enroll", "i", None))
        self.j_btn.setText(_translate("enroll", "j", None))
        self.five_btn.setText(_translate("enroll", "5", None))
        self.return_btn.setText(_translate("enroll", "Return", None))
        self.two_btn.setText(_translate("enroll", "2", None))
        self.six_btn.setText(_translate("enroll", "6", None))
        self.g_btn.setText(_translate("enroll", "g", None))
        self.c_btn.setText(_translate("enroll", "c", None))
        self.eight_btn.setText(_translate("enroll", "8", None))
        self.space_btn.setText(_translate("enroll", "Space", None))

        self.setStyleSheet(_fromUtf8(".QWidget {\n"
"background-image: url(res/images/background.jpg);\n"
"}\n"
""))
        self.shift_on = False

        self.a='a'
        self.b='b'
        self.c='c'
        self.d='d'
        self.e='e'
        self.f='f'
        self.g='g'
        self.h='h'
        self.i='i'
        self.j='j'
        self.k='k'
        self.l='l'
        self.m='m'
        self.n='n'
        self.o='o'
        self.p='p'
        self.q='q'
        self.r='r'
        self.s='s'
        self.t='t'
        self.u='u'
        self.v='v'
        self.w='w'
        self.x='x'
        self.y='y'
        self.z='z'


        self.btn_a_f='btn_a'
        self.btn_b_f='btn_b'
        self.btn_c_f='btn_c'
        self.btn_d_f='btn_d'
        self.btn_e_f='btn_e'
        self.btn_f_f='btn_f'
        self.btn_g_f='btn_g'
        self.btn_h_f='btn_h'
        self.btn_i_f='btn_i'
        self.btn_j_f='btn_j'
        self.btn_k_f='btn_k'
        self.btn_l_f='btn_l'
        self.btn_m_f='btn_m'
        self.btn_n_f='btn_n'
        self.btn_o_f='btn_o'
        self.btn_p_f='btn_p'
        self.btn_q_f='btn_q'
        self.btn_r_f='btn_r'
        self.btn_s_f='btn_s'
        self.btn_t_f='btn_t'
        self.btn_u_f='btn_u'
        self.btn_v_f='btn_v'
        self.btn_w_f='btn_w'
        self.btn_x_f='btn_x'
        self.btn_y_f='btn_y'
        self.btn_z_f='btn_z'
        self.btn_enter_f='btn_enter'
        self.btn_shift_f='btn_shift'
        self.btn_back_f='btn_back'
        self.btn_interro_f='btn_interro'
        self.btn_space_f='btn_space'
        self.btn_slash_f='btn_slash'
        self.btn_point_f='btn_point'
        self.btn_colon_f='btn_colon'
        self.btn_hyphen_f='btn_hyphen'
        self.btn_underscore_f = 'btn_underscore'
        self.btn_at_f='btn_at'
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


        def btn_back():
            line_temp = QtGui.QApplication.focusWidget()
            if ( type(line_temp) == QtGui.QLineEdit):
                 line_temp.setText(line_temp.text()[:-1])

        def btn_interro():
            line_temp = QtGui.QApplication.focusWidget()
            if ( type(line_temp) == QtGui.QLineEdit):
                 line_temp.setText(line_temp.text()+'?')
        def btn_space():
            line_temp = QtGui.QApplication.focusWidget()
            if ( type(line_temp) == QtGui.QLineEdit):
                 line_temp.setText(line_temp.text()+' ')
        def btn_slash():
            line_temp = QtGui.QApplication.focusWidget()
            if ( type(line_temp) == QtGui.QLineEdit):
                 line_temp.setText(line_temp.text()+'/')

        def btn_point():
            line_temp = QtGui.QApplication.focusWidget()
            if ( type(line_temp) == QtGui.QLineEdit):
                 line_temp.setText(line_temp.text()+'.')

        def btn_colon():
            line_temp = QtGui.QApplication.focusWidget()
            if ( type(line_temp) == QtGui.QLineEdit):
                 line_temp.setText(line_temp.text()+':')

        def btn_hyphen():
            line_temp = QtGui.QApplication.focusWidget()
            if ( type(line_temp) == QtGui.QLineEdit):
                 line_temp.setText(line_temp.text()+'-')

        def btn_underscore():
            line_temp = QtGui.QApplication.focusWidget()
            if ( type(line_temp) == QtGui.QLineEdit):
                 line_temp.setText(line_temp.text()+'_')

        def btn_at():
            line_temp = QtGui.QApplication.focusWidget()
            if ( type(line_temp) == QtGui.QLineEdit):
                 line_temp.setText(line_temp.text()+'@')

        def btn_one():
            line_temp = QtGui.QApplication.focusWidget()
            if ( type(line_temp) == QtGui.QLineEdit):
                 line_temp.setText(line_temp.text()+'1')
        def btn_two():
            line_temp = QtGui.QApplication.focusWidget()
            if ( type(line_temp) == QtGui.QLineEdit):
                 line_temp.setText(line_temp.text()+'2')

        def btn_three():
            line_temp = QtGui.QApplication.focusWidget()
            if ( type(line_temp) == QtGui.QLineEdit):
                 line_temp.setText(line_temp.text()+'3')

        def btn_four():
            line_temp = QtGui.QApplication.focusWidget()
            if ( type(line_temp) == QtGui.QLineEdit):
                 line_temp.setText(line_temp.text()+'4')

        def btn_five():
            line_temp = QtGui.QApplication.focusWidget()
            if ( type(line_temp) == QtGui.QLineEdit):
                 line_temp.setText(line_temp.text()+'5')

        def btn_six():
            line_temp = QtGui.QApplication.focusWidget()
            if ( type(line_temp) == QtGui.QLineEdit):
                 line_temp.setText(line_temp.text()+'6')

        def btn_seven():
            line_temp = QtGui.QApplication.focusWidget()
            if ( type(line_temp) == QtGui.QLineEdit):
                 line_temp.setText(line_temp.text()+'7')

        def btn_eight():
            line_temp = QtGui.QApplication.focusWidget()
            if ( type(line_temp) == QtGui.QLineEdit):
                 line_temp.setText(line_temp.text()+'8')

        def btn_nine():
            line_temp = QtGui.QApplication.focusWidget()
            if ( type(line_temp) == QtGui.QLineEdit):
                 line_temp.setText(line_temp.text()+'9')

        def btn_zero():
            line_temp = QtGui.QApplication.focusWidget()
            if ( type(line_temp) == QtGui.QLineEdit):
                 line_temp.setText(line_temp.text()+'0')

        def btn_enter():
            QtGui.QMessageBox.about(self,"Message","Button enter Clicked, The function should be here.")
            return None

        def btn_return():
            self.close()

        def btn_shift(shift=False):


            if (not shift):
                self.labels = [x.upper() for x in self.labels]
            else:
                self.labels = [x.lower() for x in self.labels]

            for button,label in zip(self.buttons_az, self.labels):
                button.setText(label)
            self.shift_on = True


        def btn_q():
            line_temp = QtGui.QApplication.focusWidget()
            if ( type(line_temp) == QtGui.QLineEdit):
                 line_temp.setText(line_temp.text() + self.labels[string.lowercase.index('q')])
                 btn_shift(True)


        def btn_a():
            line_temp = QtGui.QApplication.focusWidget()
            if ( type(line_temp) == QtGui.QLineEdit):
                line_temp.setText(line_temp.text() + self.labels[string.lowercase.index('a')])
                btn_shift(True)
        def btn_z():
            line_temp = QtGui.QApplication.focusWidget()
            if ( type(line_temp) == QtGui.QLineEdit):
                line_temp.setText(line_temp.text() + self.labels[string.lowercase.index('z')])
                btn_shift(True)
        def btn_e():
            line_temp = QtGui.QApplication.focusWidget()
            if ( type(line_temp) == QtGui.QLineEdit):
                 line_temp.setText(line_temp.text() + self.labels[string.lowercase.index('e')])
                 btn_shift(True)


        def btn_r():
            line_temp = QtGui.QApplication.focusWidget()
            if ( type(line_temp) == QtGui.QLineEdit):
                 line_temp.setText(line_temp.text() + self.labels[string.lowercase.index('r')])
                 btn_shift(True)


        def btn_t():
            line_temp = QtGui.QApplication.focusWidget()
            if ( type(line_temp) == QtGui.QLineEdit):
                 line_temp.setText(line_temp.text() + self.labels[string.lowercase.index('t')])
                 btn_shift(True)

        def btn_y():
            line_temp = QtGui.QApplication.focusWidget()
            if ( type(line_temp) == QtGui.QLineEdit):
                 line_temp.setText(line_temp.text() + self.labels[string.lowercase.index('y')])
                 btn_shift(True)

        def btn_u():
            line_temp = QtGui.QApplication.focusWidget()
            if ( type(line_temp) == QtGui.QLineEdit):
                 line_temp.setText(line_temp.text() + self.labels[string.lowercase.index('u')])
                 btn_shift(True)

        def btn_i():
            line_temp = QtGui.QApplication.focusWidget()
            if ( type(line_temp) == QtGui.QLineEdit):
                 line_temp.setText(line_temp.text() + self.labels[string.lowercase.index('i')])
                 btn_shift(True)
        def btn_o():
            line_temp = QtGui.QApplication.focusWidget()
            if ( type(line_temp) == QtGui.QLineEdit):
                 line_temp.setText(line_temp.text() + self.labels[string.lowercase.index('o')])
                 btn_shift(True)

        def btn_p():
            line_temp = QtGui.QApplication.focusWidget()
            if ( type(line_temp) == QtGui.QLineEdit):
                 line_temp.setText(line_temp.text() + self.labels[string.lowercase.index('p')])
                 btn_shift(True)

        def btn_s():
            line_temp = QtGui.QApplication.focusWidget()
            if ( type(line_temp) == QtGui.QLineEdit):
                 line_temp.setText(line_temp.text() + self.labels[string.lowercase.index('s')])
                 btn_shift(True)

        def btn_d():
            line_temp = QtGui.QApplication.focusWidget()
            if ( type(line_temp) == QtGui.QLineEdit):
                 line_temp.setText(line_temp.text() + self.labels[string.lowercase.index('d')])
                 btn_shift(True)

        def btn_f():
            line_temp = QtGui.QApplication.focusWidget()
            if ( type(line_temp) == QtGui.QLineEdit):
                 line_temp.setText(line_temp.text() + self.labels[string.lowercase.index('f')])
                 btn_shift(True)

        def btn_g():
            line_temp = QtGui.QApplication.focusWidget()
            if ( type(line_temp) == QtGui.QLineEdit):
                 line_temp.setText(line_temp.text() + self.labels[string.lowercase.index('g')])
                 btn_shift(True)

        def btn_h():
            line_temp = QtGui.QApplication.focusWidget()
            if ( type(line_temp) == QtGui.QLineEdit):
                 line_temp.setText(line_temp.text() + self.labels[string.lowercase.index('h')])
                 btn_shift(True)

        def btn_j():
            line_temp = QtGui.QApplication.focusWidget()
            if ( type(line_temp) == QtGui.QLineEdit):
                 line_temp.setText(line_temp.text() + self.labels[string.lowercase.index('j')])
                 btn_shift(True)

        def btn_k():
            line_temp = QtGui.QApplication.focusWidget()
            if ( type(line_temp) == QtGui.QLineEdit):
                 line_temp.setText(line_temp.text() + self.labels[string.lowercase.index('k')])
                 btn_shift(True)

        def btn_l():
            line_temp = QtGui.QApplication.focusWidget()
            if ( type(line_temp) == QtGui.QLineEdit):
                 line_temp.setText(line_temp.text() + self.labels[string.lowercase.index('l')])
                 btn_shift(True)
        def btn_m():
            line_temp = QtGui.QApplication.focusWidget()
            if ( type(line_temp) == QtGui.QLineEdit):
                 line_temp.setText(line_temp.text() + self.labels[string.lowercase.index('m')])
                 btn_shift(True)

        def btn_w():
            line_temp = QtGui.QApplication.focusWidget()
            if ( type(line_temp) == QtGui.QLineEdit):
                 line_temp.setText(line_temp.text() + self.labels[string.lowercase.index('w')])
                 btn_shift(True)

        def btn_x():
            line_temp = QtGui.QApplication.focusWidget()
            if ( type(line_temp) == QtGui.QLineEdit):
                 line_temp.setText(line_temp.text() + self.labels[string.lowercase.index('x')])
                 btn_shift(True)

        def btn_c():
            line_temp = QtGui.QApplication.focusWidget()
            if ( type(line_temp) == QtGui.QLineEdit):
                 line_temp.setText(line_temp.text() + self.labels[string.lowercase.index('c')])
                 btn_shift(True)

        def btn_b():
            line_temp = QtGui.QApplication.focusWidget()
            if ( type(line_temp) == QtGui.QLineEdit):
                 line_temp.setText(line_temp.text() + self.labels[string.lowercase.index('b')])
                 btn_shift(True)

        def btn_n():
            line_temp = QtGui.QApplication.focusWidget()
            if ( type(line_temp) == QtGui.QLineEdit):
                 line_temp.setText(line_temp.text() + self.labels[string.lowercase.index('n')])
                 btn_shift(True)

        def btn_v():
            line_temp = QtGui.QApplication.focusWidget()
            if ( type(line_temp) == QtGui.QLineEdit):
                 line_temp.setText(line_temp.text() + self.labels[string.lowercase.index('v')])
                 btn_shift(True)

        def btn_return():
            self.close()




        # This is the search function where you search for a User,
        #   for this part If you could send me a replica of the database you are using, I can do Its
        #   implementation, It's the fingerprint part that I won't be able to do.
        def search():
            user = self.usernameedit.text()
            with open('users.txt') as f:
                users_db = f.readlines()
            font = QtGui.QFont()
            font.setPointSize(24)



            for line in users_db:
                print " line : " + line
                print " line : " + line
                print " user : " + user
                if (user == line[:-1]):
                    print line
                    print self.userlist.addItem(line)











        self.funcs = [  self.btn_a_f,
        self.btn_b_f,
        self.btn_c_f,
        self.btn_d_f,
        self.btn_e_f,
        self.btn_f_f,
        self.btn_g_f,
        self.btn_h_f,
        self.btn_i_f,
        self.btn_j_f,
        self.btn_k_f,
        self.btn_l_f,
        self.btn_m_f,
        self.btn_n_f,
        self.btn_o_f,
        self.btn_p_f,
        self.btn_q_f,
        self.btn_r_f,
        self.btn_s_f,
        self.btn_t_f,
        self.btn_u_f,
        self.btn_v_f,
        self.btn_w_f,
        self.btn_x_f,
        self.btn_y_f,
        self.btn_z_f,
        self.btn_enter_f,
        self.btn_shift_f,
        self.btn_back_f,
        self.btn_interro_f,
        self.btn_space_f,
        self.btn_slash_f,
        self.btn_point_f,
        self.btn_colon_f,
        self.btn_hyphen_f,
        self.btn_underscore_f,
        self.btn_at_f,
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
        self.other_buttons = [
            self.enter_btn,
            self.shift_btn,
            self.back_btn,
            self.interro_btn,
            self.space_btn,
            self.slash_btn,
            self.point_btn,
            self.colon_btn,
            self.hyphen_btn,
            self.underscore_btn,
            self.at_btn,
            self.one_btn,
            self.two_btn,
            self.three_btn,
            self.four_btn,
            self.five_btn,
            self.six_btn,
            self.seven_btn,
            self.eight_btn,
            self.nine_btn,
            self.zero_btn,
            self.at_btn

        ]




        self.buttons_az = [ self.a_btn,
        self.b_btn,
        self.c_btn,
        self.d_btn,
        self.e_btn,
        self.f_btn,
        self.g_btn,
        self.h_btn,
        self.i_btn,
        self.j_btn,
        self.k_btn,
        self.l_btn,
        self.m_btn,
        self.n_btn,
        self.o_btn,
        self.p_btn,
        self.q_btn,
        self.r_btn,
        self.s_btn,
        self.t_btn,
        self.u_btn,
        self.v_btn,
        self.w_btn,
        self.x_btn,
        self.y_btn,
        self.z_btn
                         ]

        self.labels = [  self.a,
        self.b,
        self.c,
        self.d,
        self.e,
        self.f,
        self.g,
        self.h,
        self.i,
        self.j,
        self.k,
        self.l,
        self.m,
        self.n,
        self.o,
        self.p,
        self.q,
        self.r,
        self.s,
        self.t,
        self.u,
        self.v,
        self.w,
        self.x,
        self.y,
        self.z,
                         ]
        for button in self.buttons_az:
            button.setFocusPolicy(QtCore.Qt.NoFocus)


        for button in self.other_buttons:
            button.setFocusPolicy(QtCore.Qt.NoFocus)
        self.button_css= """

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


        """

        self.search_button.setStyleSheet(self.button_css)
        self.search_button.clicked.connect(search)

        self.return_btn.setStyleSheet(self.button_css)
        self.return_btn.clicked.connect(btn_return)




        self.buttons = self.buttons_az+self.other_buttons
        for (button,func) in zip(self.buttons,self.funcs):

            button.clicked.connect(locals()[func])
            button.setStyleSheet(self.button_css)


        self.return_btn.clicked.connect(btn_return)


if __name__ == '__main__':
    app = QtGui.QApplication(sys.argv)
    ex = Ui_enroll()
    ex.show()
    #To launch the application in full screen mode, Comment the previous line and Uncomment the next one.
    #ex.showFullScreen()

    sys.exit(app.exec_())