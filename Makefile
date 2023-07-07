# ============================================================================ #
#                                                                              #
#                                                               /   /   \      #
#   Made By IsCoffeeTho (Aaron Menadue)                       /    |      \    #
#                                                            |     |       |   #
#   Makefile                                                 |      \      |   #
#                                                            |       |     |   #
#   Last Edited: 12:07AM 06/07/2023                           \      |    /    #
#                                                               \   /   /      #
#                                                                              #
# ============================================================================ #

all:
	mvn package -f ./pom.xml
	mv target/stm*.jar ..

clean:
	rm -rf target

fclean: clean
	-rm ../stm*.jar

re: fclean all

.PHONY: all clean fclean re