# ============================================================================ #
#                                                                              #
#                                                               /   /   \      #
#   Made By IsCoffeeTho (Aaron Menadue)                       /    |      \    #
#                                                            |     |       |   #
#   Makefile                                                 |      \      |   #
#                                                            |       |     |   #
#   Last Edited: 07:42AM 06/07/2023                           \      |    /    #
#                                                               \   /   /      #
#                                                                              #
# ============================================================================ #

all:
	mvn package -f ./pom.xml
	mv target/stm*.jar ..

clean:
	rm -rf target/classes
	rm -rf target/maven-archiver
	rm -rf target/maven-status
	rm -rf target/surefire-status
	rm -rf target/test-classes

fclean: clean
	-rm ../stm*.jar

re: fclean all

.PHONY: all clean fclean re