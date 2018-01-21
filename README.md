
 Запуск пориложения из системы сборки (SBT)
 в консоле перейти в папку проекта parkplace-monitor
 выполнить команду
 sbt run

 при необходимости собрать архив  дистрибутива -
 sbt dist
 архив выкладывается в папку parkplace-monitor/target/universal

 для запуска приложения из дистрибутива 
 (необходима 8я JAVA, на 9й не раоботает!)
 1 распаковать архив
 2 выполнить команду
 юникс/mac
 bin/parkplace-monitor -Dplay.http.secret.key='123'

 bin/parkplace-monitor.bat -Dplay.http.secret.key='123'

 интерфейс приложения доступен по урл -
 http://localhost:9000/
 
 
 собраный дистрибутив доступен по адресу
 https://yadi.sk/d/W7dwK-td3Rev84