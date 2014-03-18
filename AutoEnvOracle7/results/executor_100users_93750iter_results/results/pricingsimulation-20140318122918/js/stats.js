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
		total: "4375",
		ok: "4375",
		ko: "0"
	},
	minResponseTime : {
		total: "560",
		ok: "560",
		ko: "-"
	},
	maxResponseTime : {
		total: "1720",
		ok: "1720",
		ko: "-"
	},
	meanResponseTime : {
		total: "686",
		ok: "686",
		ko: "-"
	},
	standardDeviation : {
		total: "103",
		ok: "103",
		ko: "-"
	},
	percentiles1 : {
		total: "760",
		ok: "760",
		ko: "-"
	},
	percentiles2 : {
		total: "1320",
		ok: "1320",
		ko: "-"
	},
	group1 : {
		name: "t < 800 ms",
		count: 4274,
		percentage: 97
	},
	group2 : {
		name: "800 ms < t < 1200 ms",
		count: 40,
		percentage: 0
	},
	group3 : {
		name: "t > 1200 ms",
		count: 61,
		percentage: 1
	},
	group4 : {
		name: "failed",
		count: 0,
		percentage: 0
	},
	meanNumberOfRequestsPerSecond: {
		total: "143",
		ok: "143",
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
		total: "4375",
		ok: "4375",
		ko: "0"
	},
	minResponseTime : {
		total: "560",
		ok: "560",
		ko: "-"
	},
	maxResponseTime : {
		total: "1720",
		ok: "1720",
		ko: "-"
	},
	meanResponseTime : {
		total: "686",
		ok: "686",
		ko: "-"
	},
	standardDeviation : {
		total: "103",
		ok: "103",
		ko: "-"
	},
	percentiles1 : {
		total: "760",
		ok: "760",
		ko: "-"
	},
	percentiles2 : {
		total: "1320",
		ok: "1320",
		ko: "-"
	},
	group1 : {
		name: "t < 800 ms",
		count: 4274,
		percentage: 97
	},
	group2 : {
		name: "800 ms < t < 1200 ms",
		count: 40,
		percentage: 0
	},
	group3 : {
		name: "t > 1200 ms",
		count: 61,
		percentage: 1
	},
	group4 : {
		name: "failed",
		count: 0,
		percentage: 0
	},
	meanNumberOfRequestsPerSecond: {
		total: "143",
		ok: "143",
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
