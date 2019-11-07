from tkinter import *
import ftplib
import os
import sys
from datetime import datetime

def download(file):
    ftp = ftplib.FTP("ftpupload.net")
    ftp.login("epiz_23738700", "7ci7x2Qw")
    ftp.cwd("/htdocs/")
    ftp.retrbinary("RETR " + file, open(file, "wb").write)
    ftp.quit()

def upload(file):
    ftp = ftplib.FTP("ftpupload.net")
    ftp.login("epiz_23738700", "7ci7x2Qw")
    ftp.cwd("/htdocs/")
    ftp.storbinary("STOR " + file, open(file, "rb"), 1024)
    ftp.quit()

def delete(file):
    ftp = ftplib.FTP("ftpupload.net")
    ftp.login("epiz_23738700", "7ci7x2Qw")
    ftp.cwd("/htdocs/")
    ftp.delete(file)
    ftp.quit()
    
login = ""
root = Tk()
f = Frame(root, width = 300, height = 300, bg = "yellow")
u = Entry(root)
u.place(x = 70, y = 60, width = 180)
p = Entry(root, font = "bold")
p.place(x = 70, y = 90, width = 180)
l = Label(root, font = "bold")
l.place(x = 100, y = 220)
def ba():
                global login
                login = u.get()
                pasw = p.get()
                l.config(text = "LOADING...")
                download("name.txt")
                users = open("name.txt", "r").read()
                os.remove("name.txt")
                download("password.txt")
                passes = open("password.txt", "r").read()
                os.remove("password.txt")
                if login in users:
                                if pasw + login in passes:
                                                l.config(text = "Hi, " + login)
                                else:
                                                l.config(text = "WRONG PASSWORD")
                else:
                                l.config(text = "USER DOES NOT EXIST")
                                
b = Button(text = "LOGIN", font = "bold 15", command = ba).place(x = 90, y = 140)
f.pack()
root.mainloop()
