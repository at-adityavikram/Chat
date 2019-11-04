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
    
def printc(text):
	os.system("clear")
	print(text)

print("Loading...")
download("name.txt")
name = open("name.txt", "r").read()
os.remove("name.txt")
download("password.txt")    
pswd = open("password.txt", "r").read()
os.remove("password.txt")
os.system("clear")
nae = input("Enter Username: ")
if nae in name:
	pas = input("Enter Password: ")
	if pas + nae in pswd:
		os.system("clear")
		print("Hello,", nae)
		print()
		user = ""
		while user == "" and nae != "":
			user = input("Enter Name Of User To Chat With: ")
			if user == nae:
				user = ""
				print("Can't Chat With Yourself")
			else:
				if user in name:
					while user != "":
						os.system("clear")
						print("Loading...")
						a = nae + user + ".txt"
						b = user + nae + ".txt"
						try:
							download(a)
							download(b)
							lgn = open(a, "r").readlines()
							usr = open(b, "r").readlines()
							tie = {}
							for i in range(len(lgn)):
								y = lgn[i]
								state = 0
								time = ""
								msg = ""
								for j in range(len(y)):
									if y[j] != " " and state == 0:
										time += y[j]
									elif y[j] != " " and state == 1:
										msg += y[j]
									elif y[j] == " " and state == 1:
										msg += y[j]
									else:
										state = 1
								timei = int(time)
								msg = msg.replace("\n", "")
								tie.update({timei : msg})
							tier = {}
							for z in range(len(usr)):
								y = usr[z]
								state = 0
								time = ""
								msg = ""
								for j in range(len(y)):
									if y[j] != " " and state == 0:
										time += y[j]
									elif y[j] != " " and state == 1:
										msg += y[j]
									elif y[j] == " " and state == 1:
										msg += y[j]
									else:
										state = 1
								timei = int(time)
								msg = msg.replace("\n", "")
								tier.update({timei : msg})
							tieo = {**tie, **tier}
							ties = sorted(tieo, key = tieo.get)
							ties.sort()
							os.remove(a)
							os.remove(b)
							print(tieo)
							print(ties)
							for k in range(len(ties)):
								print(tieo.get(ties[k]))
							sms = input("Send Message: ")
							print("Loading...")
							download(a)
							gh = open(a, "a")
							t3 = str(datetime.now())
							t3 = t3.replace(":", "")
							t3 = t3.replace("-", "")
							t3 = t3.replace(".", "")
							t3 = t3.replace(" ", "")
							gh.write("\n" + t3 + " " + nae + ": " + sms)
							gh.close()
							upload(a)
							os.remove(a)
						except:
							t = str(datetime.now())
							t = t.replace(":", "")
							t = t.replace("-", "")
							t = t.replace(".", "")
							t = t.replace(" ", "")
							f = open(a, "w")
							f.write(t + " " + nae + ": Test")
							f.close()
							upload(a)
							os.remove(a)
							t1 = str(datetime.now())
							t1 = t1.replace(":", "")
							t1 = t1.replace("-", "")
							t1 = t1.replace(".", "")
							t1 = t1.replace(" ", "")
							f1 = open(b, "w")
							f1.write(t1 + " " + user + ": Test")
							f1.close()
							upload(b)
							os.remove(b)
							download(a)
							download(b)
							lgn = open(a, "r").readlines()
							usr = open(b, "r").readlines()
							os.remove(a)
							os.remove(b)
					
				else:
					user = ""
					print("User Does Not Exist")
	else:
		print("Wrong Password")
else:
	print("User Does Not Exist")