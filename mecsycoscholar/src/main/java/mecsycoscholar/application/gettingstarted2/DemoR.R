getwd()
data<-read.csv2("data_log/result.csv", header = FALSE, sep = ";", quote="\"", dec=".",blank.lines.skip=TRUE,fill=TRUE)

names(data)[1]<-c("Time")
for (i in 2:length(data)){
names(data)[i]<-c(paste0("Value",as.character(i-1)))
}

for (i in 2:length(data)){
Header<-names(data)[i]
NumValue<-sapply(data[i],as.numeric)
tmpMean<-mean(NumValue)
tmpMin<-min(NumValue)
tmpMax<-max(NumValue)

if(i==2){
result<-data.frame(Series=Header,Min=tmpMin,Max=tmpMax,Mean=tmpMean)
}

else{
Header2<-c(as.character(result$"Series"),names(data)[i])
tmp2Min<-c(result$"Min",tmpMin)
tmp2Max<-c(result$"Max",tmpMax)
tmp2Mean<-c(result$"Mean",tmpMean)
result<-data.frame(Series=Header2,Min=tmp2Min,Max=tmp2Max,Mean=tmp2Mean)
}

}

write.csv2(result,file="data_log/ResultR.csv")



