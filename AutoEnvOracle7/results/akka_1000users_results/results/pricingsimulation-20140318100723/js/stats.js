var stats = {
	type: "GROUP",
contents: {
		
				"price-78a5eb43deef9a7b5b9ce157b9d52ac4": {
		type: "REQUEST",
		name: "price",
path: "price",
pathFormatted: "price-78a5eb43deef9a7b5b9ce157b9d52ac4",
stats: {
	numberOfRequests : {
		total: "1805",
		ok: "1290",
		ko: "515"
	},
	minResponseTime : {
		total: "0",
		ok: "1730",
		ko: "0"
	},
	maxResponseTime : {
		total: "62280",
		ok: "62280",
		ko: "60040"
	},
	meanResponseTime : {
		total: "15419",
		ok: "15059",
		ko: "16320"
	},
	standardDeviation : {
		total: "1787",
		ok: "0",
		ko: "7216"
	},
	percentiles1 : {
		total: "46460",
		ok: "45240",
		ko: "60010"
	},
	percentiles2 : {
		total: "60020",
		ok: "47220",
		ko: "60030"
	},
	group1 : {
		name: "t < 800 ms",
		count: 0,
		percentage: 0
	},
	group2 : {
		name: "800 ms < t < 1200 ms",
		count: 0,
		percentage: 0
	},
	group3 : {
		name: "t > 1200 ms",
		count: 1290,
		percentage: 71
	},
	group4 : {
		name: "failed",
		count: 515,
		percentage: 28
	},
	meanNumberOfRequestsPerSecond: {
		total: "29",
		ok: "20",
		ko: "8"
	}
}


	}
		},
name: "Global Information",
path: "",
pathFormatted: "missing-name",
stats: {
	numberOfRequests : {
		total: "1805",
		ok: "1290",
		ko: "515"
	},
	minResponseTime : {
		total: "0",
		ok: "1730",
		ko: "0"
	},
	maxResponseTime : {
		total: "62280",
		ok: "62280",
		ko: "60040"
	},
	meanResponseTime : {
		total: "15419",
		ok: "15059",
		ko: "16320"
	},
	standardDeviation : {
		total: "1787",
		ok: "0",
		ko: "7216"
	},
	percentiles1 : {
		total: "46460",
		ok: "45240",
		ko: "60010"
	},
	percentiles2 : {
		total: "60020",
		ok: "47220",
		ko: "60030"
	},
	group1 : {
		name: "t < 800 ms",
		count: 0,
		percentage: 0
	},
	group2 : {
		name: "800 ms < t < 1200 ms",
		count: 0,
		percentage: 0
	},
	group3 : {
		name: "t > 1200 ms",
		count: 1290,
		percentage: 71
	},
	group4 : {
		name: "failed",
		count: 515,
		percentage: 28
	},
	meanNumberOfRequestsPerSecond: {
		total: "29",
		ok: "20",
		ko: "8"
	}
}



}

function fillStats(stat){
    $("#numberOfRequests").append(stat.numberOfRequests.total);
    $("#numberOfRequestsOK").append(stat.numberOfRequests.ok);
    $("#numberOfRequestsKO").append(stat.numberOfRequests.ko);

    $("#minResponseTime").append(stat.minResponseTime.total);
    $("#minResponseTimeOK").append(stat.minResponseTime.ok);
    $("#minResponseTimeKO").append(stat.minResponseTime.ko);

    $("#maxResponseTime").append(stat.maxResponseTime.total);
    $("#maxResponseTimeOK").append(stat.maxResponseTime.ok);
    $("#maxResponseTimeKO").append(stat.maxResponseTime.ko);

    $("#meanResponseTime").append(stat.meanResponseTime.total);
    $("#meanResponseTimeOK").append(stat.meanResponseTime.ok);
    $("#meanResponseTimeKO").append(stat.meanResponseTime.ko);

    $("#standardDeviation").append(stat.standardDeviation.total);
    $("#standardDeviationOK").append(stat.standardDeviation.ok);
    $("#standardDeviationKO").append(stat.standardDeviation.ko);

    $("#percentiles1").append(stat.percentiles1.total);
    $("#percentiles1OK").append(stat.percentiles1.ok);
    $("#percentiles1KO").append(stat.percentiles1.ko);

    $("#percentiles2").append(stat.percentiles2.total);
    $("#percentiles2OK").append(stat.percentiles2.ok);
    $("#percentiles2KO").append(stat.percentiles2.ko);

    $("#meanNumberOfRequestsPerSecond").append(stat.meanNumberOfRequestsPerSecond.total);
    $("#meanNumberOfRequestsPerSecondOK").append(stat.meanNumberOfRequestsPerSecond.ok);
    $("#meanNumberOfRequestsPerSecondKO").append(stat.meanNumberOfRequestsPerSecond.ko);
}
