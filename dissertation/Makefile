BASENAME=dissertation
NAME=$(BASENAME).tex
PDF=$(BASENAME).pdf
LATEX_COMPILER=pdflatex
GLOSSARIES_COMPILER=makeglossaries
BIB_COMPILER=bibtex

all: build

pdf:
	$(LATEX_COMPILER) $(NAME)

bibliography: pdf
	$(BIB_COMPILER) $(BASENAME)

bibliography_pdf: bibliography
	$(LATEX_COMPILER) $(NAME)
	$(LATEX_COMPILER) $(NAME)

glossaries: pdf
	$(GLOSSARIES_COMPILER) $(BASENAME)

glossaries_pdf: glossaries
	$(LATEX_COMPILER) $(NAME)

build: pdf glossaries bibliography_pdf

clean:
	sh clean.sh
