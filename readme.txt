� maven/conf/settings.xml ��������� (��� ������������� ������� jetty):
	<pluginGroups>
		<pluginGroup>org.mortbay.jetty</pluginGroup>
	</pluginGroups>

������: 
	mvn clean package
��������� ������ ��. � ����� target.

������ � ������ �������: 
	mvn clean package jetty:run
���������� ����������� �� ������� ����� � ����� ������ �� ����� 80, �.�. ��� ������� ���������� � �������� ���������� �������:
	http://localhost
