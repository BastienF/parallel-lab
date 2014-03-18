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
		total: "38",
		ok: "38",
		ko: "0"
	},
	minResponseTime : {
		total: "570",
		ok: "570",
		ko: "-"
	},
	maxResponseTime : {
		total: "19790",
		ok: "19790",
		ko: "-"
	},
	meanResponseTime : {
		total: "2389",
		ok: "2389",
		ko: "-"
	},
	standardDeviation : {
		total: "5058",
		ok: "5058",
		ko: "-"
	},
	percentiles1 : {
		total: "19470",
		ok: "19470",
		ko: "-"
	},
	percentiles2 : {
		total: "19790",
		ok: "19790",
		ko: "-"
	},
	group1 : {
		name: "t < 800 ms",
		count: 2,
		percentage: 5
	},
	group2 : {
		name: "800 ms < t < 1200 ms",
		count: 33,
		percentage: 86
	},
	group3 : {
		name: "t > 1200 ms",
		count: 3,
		percentage: 7
	},
	group4 : {
		name: "failed",
		count: 0,
		percentage: 0
	},
	meanNumberOfRequestsPerSecond: {
		total: "1",
		ok: "1",
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
		total: "38",
		ok: "38",
		ko: "0"
	},
	minResponseTime : {
		total: "570",
		ok: "570",
		ko: "-"
	},
	maxResponseTime : {
		total: "19790",
		ok: "19790",
		ko: "-"
	},
	meanResponseTime : {
		total: "2389",
		ok: "2389",
		ko: "-"
	},
	standardDeviation : {
		total: "5058",
		ok: "5058",
		ko: "-"
	},
	percentiles1 : {
		total: "19470",
		ok: "19470",
		ko: "-"
	},
	percentiles2 : {
		total: "19790",
		ok: "19790",
		ko: "-"
	},
	group1 : {
		name: "t < 800 ms",
		count: 2,
		percentage: 5
	},
	group2 : {
		name: "800 ms < t < 1200 ms",
		count: 33,
		percentage: 86
	},
	group3 : {
		name: "t > 1200 ms",
		count: 3,
		percentage: 7
	},
	group4 : {
		name: "failed",
		count: 0,
		percentage: 0
	},
	meanNumberOfRequestsPerSecond: {
		total: "1",
		ok: "1",
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
