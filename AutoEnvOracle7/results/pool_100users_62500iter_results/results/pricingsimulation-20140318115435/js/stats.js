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
		total: "5185",
		ok: "5185",
		ko: "0"
	},
	minResponseTime : {
		total: "390",
		ok: "390",
		ko: "-"
	},
	maxResponseTime : {
		total: "1850",
		ok: "1850",
		ko: "-"
	},
	meanResponseTime : {
		total: "576",
		ok: "576",
		ko: "-"
	},
	standardDeviation : {
		total: "124",
		ok: "124",
		ko: "-"
	},
	percentiles1 : {
		total: "650",
		ok: "650",
		ko: "-"
	},
	percentiles2 : {
		total: "1400",
		ok: "1400",
		ko: "-"
	},
	group1 : {
		name: "t < 800 ms",
		count: 5085,
		percentage: 98
	},
	group2 : {
		name: "800 ms < t < 1200 ms",
		count: 28,
		percentage: 0
	},
	group3 : {
		name: "t > 1200 ms",
		count: 72,
		percentage: 1
	},
	group4 : {
		name: "failed",
		count: 0,
		percentage: 0
	},
	meanNumberOfRequestsPerSecond: {
		total: "170",
		ok: "170",
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
		total: "5185",
		ok: "5185",
		ko: "0"
	},
	minResponseTime : {
		total: "390",
		ok: "390",
		ko: "-"
	},
	maxResponseTime : {
		total: "1850",
		ok: "1850",
		ko: "-"
	},
	meanResponseTime : {
		total: "576",
		ok: "576",
		ko: "-"
	},
	standardDeviation : {
		total: "124",
		ok: "124",
		ko: "-"
	},
	percentiles1 : {
		total: "650",
		ok: "650",
		ko: "-"
	},
	percentiles2 : {
		total: "1400",
		ok: "1400",
		ko: "-"
	},
	group1 : {
		name: "t < 800 ms",
		count: 5085,
		percentage: 98
	},
	group2 : {
		name: "800 ms < t < 1200 ms",
		count: 28,
		percentage: 0
	},
	group3 : {
		name: "t > 1200 ms",
		count: 72,
		percentage: 1
	},
	group4 : {
		name: "failed",
		count: 0,
		percentage: 0
	},
	meanNumberOfRequestsPerSecond: {
		total: "170",
		ok: "170",
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
