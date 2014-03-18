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
		total: "841",
		ok: "841",
		ko: "0"
	},
	minResponseTime : {
		total: "70",
		ok: "70",
		ko: "-"
	},
	maxResponseTime : {
		total: "3640",
		ok: "3640",
		ko: "-"
	},
	meanResponseTime : {
		total: "894",
		ok: "894",
		ko: "-"
	},
	standardDeviation : {
		total: "505",
		ok: "505",
		ko: "-"
	},
	percentiles1 : {
		total: "1730",
		ok: "1730",
		ko: "-"
	},
	percentiles2 : {
		total: "2370",
		ok: "2370",
		ko: "-"
	},
	group1 : {
		name: "t < 800 ms",
		count: 376,
		percentage: 44
	},
	group2 : {
		name: "800 ms < t < 1200 ms",
		count: 261,
		percentage: 31
	},
	group3 : {
		name: "t > 1200 ms",
		count: 204,
		percentage: 24
	},
	group4 : {
		name: "failed",
		count: 0,
		percentage: 0
	},
	meanNumberOfRequestsPerSecond: {
		total: "27",
		ok: "27",
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
		total: "841",
		ok: "841",
		ko: "0"
	},
	minResponseTime : {
		total: "70",
		ok: "70",
		ko: "-"
	},
	maxResponseTime : {
		total: "3640",
		ok: "3640",
		ko: "-"
	},
	meanResponseTime : {
		total: "894",
		ok: "894",
		ko: "-"
	},
	standardDeviation : {
		total: "505",
		ok: "505",
		ko: "-"
	},
	percentiles1 : {
		total: "1730",
		ok: "1730",
		ko: "-"
	},
	percentiles2 : {
		total: "2370",
		ok: "2370",
		ko: "-"
	},
	group1 : {
		name: "t < 800 ms",
		count: 376,
		percentage: 44
	},
	group2 : {
		name: "800 ms < t < 1200 ms",
		count: 261,
		percentage: 31
	},
	group3 : {
		name: "t > 1200 ms",
		count: 204,
		percentage: 24
	},
	group4 : {
		name: "failed",
		count: 0,
		percentage: 0
	},
	meanNumberOfRequestsPerSecond: {
		total: "27",
		ok: "27",
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
