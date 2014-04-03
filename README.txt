================================================
This is a Delay Tolerant Networking implementation on the Android platform.
This project based on bytawalla which based on DTN2.
This implementation is written in Java and highly inspired by the design of
DTN2 software version 2.6 developed in C++ by the DTN research group 
[http://www.dtnrg.org/wiki/Code].

Standards & Compatiblity
========================
This implementation followed the Bundle Protocol specification (RFC5050) and have been tested communicating 
with DTN2( Reference Implementation done by DTN Research Group). The TCP convergence layer is based on the Delay Tolerant Networking 
TCP Convergence Layer Protocol draft version 2. 

Official RFC5050 specification can be found at [http://tools.ietf.org/html/rfc5050].
Official Delay Tolerant Networking TCP Convergence Layer Protocol specification can be found at [http://tools.ietf.org/html/draft-irtf-dtnrg-tcp-clayer-02].

Folder Structure
==========================
src/		 - main source folder. All the source code for the Android platform is here.
assets/      - Android Asset folder [http://developer.android.com/guide/topics/resources/index.html].
             - The main dtn configuration file (dtn.config.xml) and its XML Schema (dtn.config.xsd) are here.          
res/         - Android Resources folder [http://developer.android.com/guide/topics/resources/index.html].
res/drawable - The pictures used in the user interfaces are stored here
res/layout   - The layout of DTN user interfaces including DTNManager, DTNConfigEditor, DTNSend, and DTNReceiveare are here.  
res/values   - The development parameters (strings.xml) stored here
bin/         - generated binary location which is suitable to run on the Android device or Emulator kept here 

Developer Guide
===============
  We used Eclispe with ADT plugin [http://developer.android.com/guide/developing/eclipse-adt.html] as a main development tool. 
As a result, continuing development with the tool is recommended but not mandatory. 

Documentation
=============
	Documentations for this software are available online from the project website.
1. Installation guide can be found at [http://www.tslab.ssvl.kth.se/csd/projects/092106/sites/default/files/Bytewalla_Installation_Guide.pdf].
2. User manual can be found at [http://www.tslab.ssvl.kth.se/csd/projects/092106/sites/default/files/Bytewalla_User_Manual.pdf].
3. Screenshots can be found at [http://www.tslab.ssvl.kth.se/csd/projects/092106/screenshot].
4. Javadoc for this software can be found at [http://www.tslab.ssvl.kth.se/csd/projects/092106/sites/default/files/Bytewalla%20Javadoc%20of%20AndroidDTN%20v1.0%20(2009.12.31).zip].
5. The system requirement to run this software can be found at [http://www.tslab.ssvl.kth.se/csd/projects/092106/sites/default/files/Bytewalla_System_Requirement.pdf].
6. This software have been tested an integration with Postfix email system. The system design architecture document
for the integration can be found at [http://www.tslab.ssvl.kth.se/csd/projects/092106/sites/default/files/Bytewalla%20System%20Architecture%20Design%20v1.0%202009.09.15.pdf].
The network setup documents before the integration can be done can be found at [http://www.tslab.ssvl.kth.se/csd/projects/092106/networkdocs].
7. Technical demonstration video can be found at [http://www.tslab.ssvl.kth.se/csd/projects/092106/tech_video]

以下为重现工作步骤以及遇到的问题
================================
1、下载代码目录，并将其import进eclipse中
	可能产生的错误：
	a.缺少import文件R.java，代码目录中本来有此文件，Errors running builder 'Android Pre Compiler' on project导致R.java文件丢失
	解决方法：取消eclipse的build Automatically功能，修改程序中的错误，重新运行就会自动生成R.java文件
	b.程序运行时总弹出窗口
		Errors occurred during the build.
			Errors running builder ‘Android Pre Complier’ on project ‘GeoSVR-DTN’.
			java .lang.NullPointerException

		解决方法：
1.Open properties of project in Eclipse then Resources -> Resource filters.
		2.Click the "Add..." button -> Check "Exclude all", "Files and folders", "All children". In the text entry box input ".svn" (without quotes).
		3.Restart Eclipse，再重新导入工程即可
	c.代码错误，若由于下载代码时有编码问题，则可能造成代码错误，这时请按照网站上的源码进行修改（使用最新版本的代码）。或者将工程导入到eclipse中后改变工程的编码类型。
2、错误完全解决后，在eclipse中用连接在计算机上的手机执行程序，即完成程序在手机上的安装及运行
3、在手机上成功安装完应用后，将手机连接到计算机上运行脚本：2@不加载AODV - 副本.bat（注意：手机的物理地址与学长实验时候的不同，要适当修改脚本），这时，可以在cmd中运行adb devices指令，查看ip地址的指定是否成功。不运行脚本的后果：软件的部分按钮根本不能用。
4、应用成功安装并顺利运行之后就要检测应用的功能，发现消息的发送与接收都不正常工作。经查看代码发现手机中缺少文件：/sdcard/test_0.5M.mp3。制作这样一个文件并push到正确位置中，程序即正常运行。现唯一不足：重复接收数据。
5、发现代码中有一处不必要的循环，是学长在开发时测试所用。在删除此段循环后，软件正常工作。

