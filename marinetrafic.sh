#!/bin/bash
while (true)
do
        java MarineTraficCrawler ships2.csv
	java MarineTraficCrawler fishingships.csv
	sleep 86400

done
