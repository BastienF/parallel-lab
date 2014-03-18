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
		total: "1664",
		ok: "1664",
		ko: "0"
	},
	minResponseTime : {
		total: "980",
		ok: "980",
		ko: "-"
	},
	maxResponseTime : {
		total: "2900",
		ok: "2900",
		ko: "-"
	},
	meanResponseTime : {
		total: "1843",
		ok: "1843",
		ko: "-"
	},
	standardDeviation : {
		total: "143",
		ok: "143",
		ko: "-"
	},
	percentiles1 : {
		total: "1930",
		ok: "1930",
		ko: "-"
	},
	percentiles2 : {
		total: "2520",
		ok: "2520",
		ko: "-"
	},
	group1 : {
		name: "t < 800 ms",
		count: 0,
		percentage: 0
	},
	group2 : {
		name: "800 ms < t < 1200 ms",
		count: 11,
		percentage: 0
	},
	group3 : {
		name: "t > 1200 ms",
		count: 1653,
		percentage: 99
	},
	group4 : {
		name: "failed",
		count: 0,
		percentage: 0
	},
	meanNumberOfRequestsPerSecond: {
		total: "52",
		ok: "52",
		ko: "-"
	}
}


	}
		},
name: "Global Information",
path: "",
pathFormatted: "missing-name",
stats: {
	numberOfRequests : {
		total: "1664",
		ok: "1664",
		ko: "0"
	},
	minResponseTime : {
		total: "980",
		ok: "980",
		ko: "-"
	},
	maxResponseTime : {
		total: "2900",
		ok: "2900",
		ko: "-"
	},
	meanResponseTime : {
		total: "1843",
		ok: "1843",
		ko: "-"
	},
	standardDeviation : {
		total: "143",
		ok: "143",
		ko: "-"
	},
	percentiles1 : {
		total: "1930",
		ok: "1930",
		ko: "-"
	},
	percentiles2 : {
		total: "2520",
		ok: "2520",
		ko: "-"
	},
	group1 : {
		name: "t < 800 ms",
		count: 0,
		percentage: 0
	},
	group2 : {
		name: "800 ms < t < 1200 ms",
		count: 11,
		percentage: 0
	},
	group3 : {
		name: "t > 1200 ms",
		count: 1653,
		percentage: 99
	},
	group4 : {
		name: "failed",
		count: 0,
		percentage: 0
	},
	meanNumberOfRequestsPerSecond: {
		total: "52",
		ok: "52",
		ko: "-"
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
