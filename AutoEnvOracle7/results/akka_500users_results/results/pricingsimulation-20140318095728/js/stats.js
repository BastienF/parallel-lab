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
		total: "1211",
		ok: "1159",
		ko: "52"
	},
	minResponseTime : {
		total: "0",
		ok: "3140",
		ko: "0"
	},
	maxResponseTime : {
		total: "56210",
		ok: "56210",
		ko: "0"
	},
	meanResponseTime : {
		total: "14090",
		ok: "14722",
		ko: "0"
	},
	standardDeviation : {
		total: "7360",
		ok: "6877",
		ko: "0"
	},
	percentiles1 : {
		total: "42980",
		ok: "43060",
		ko: "0"
	},
	percentiles2 : {
		total: "54530",
		ok: "54530",
		ko: "0"
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
		count: 1159,
		percentage: 95
	},
	group4 : {
		name: "failed",
		count: 52,
		percentage: 4
	},
	meanNumberOfRequestsPerSecond: {
		total: "20",
		ok: "19",
		ko: "1"
	}
}


	}
		},
name: "Global Information",
path: "",
pathFormatted: "missing-name",
stats: {
	numberOfRequests : {
		total: "1211",
		ok: "1159",
		ko: "52"
	},
	minResponseTime : {
		total: "0",
		ok: "3140",
		ko: "0"
	},
	maxResponseTime : {
		total: "56210",
		ok: "56210",
		ko: "0"
	},
	meanResponseTime : {
		total: "14090",
		ok: "14722",
		ko: "0"
	},
	standardDeviation : {
		total: "7360",
		ok: "6877",
		ko: "0"
	},
	percentiles1 : {
		total: "42980",
		ok: "43060",
		ko: "0"
	},
	percentiles2 : {
		total: "54530",
		ok: "54530",
		ko: "0"
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
		count: 1159,
		percentage: 95
	},
	group4 : {
		name: "failed",
		count: 52,
		percentage: 4
	},
	meanNumberOfRequestsPerSecond: {
		total: "20",
		ok: "19",
		ko: "1"
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
