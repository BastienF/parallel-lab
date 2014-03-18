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
		total: "926",
		ok: "926",
		ko: "0"
	},
	minResponseTime : {
		total: "580",
		ok: "580",
		ko: "-"
	},
	maxResponseTime : {
		total: "1310",
		ok: "1310",
		ko: "-"
	},
	meanResponseTime : {
		total: "815",
		ok: "815",
		ko: "-"
	},
	standardDeviation : {
		total: "67",
		ok: "67",
		ko: "-"
	},
	percentiles1 : {
		total: "900",
		ok: "900",
		ko: "-"
	},
	percentiles2 : {
		total: "1050",
		ok: "1050",
		ko: "-"
	},
	group1 : {
		name: "t < 800 ms",
		count: 337,
		percentage: 36
	},
	group2 : {
		name: "800 ms < t < 1200 ms",
		count: 583,
		percentage: 62
	},
	group3 : {
		name: "t > 1200 ms",
		count: 6,
		percentage: 0
	},
	group4 : {
		name: "failed",
		count: 0,
		percentage: 0
	},
	meanNumberOfRequestsPerSecond: {
		total: "30",
		ok: "30",
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
		total: "926",
		ok: "926",
		ko: "0"
	},
	minResponseTime : {
		total: "580",
		ok: "580",
		ko: "-"
	},
	maxResponseTime : {
		total: "1310",
		ok: "1310",
		ko: "-"
	},
	meanResponseTime : {
		total: "815",
		ok: "815",
		ko: "-"
	},
	standardDeviation : {
		total: "67",
		ok: "67",
		ko: "-"
	},
	percentiles1 : {
		total: "900",
		ok: "900",
		ko: "-"
	},
	percentiles2 : {
		total: "1050",
		ok: "1050",
		ko: "-"
	},
	group1 : {
		name: "t < 800 ms",
		count: 337,
		percentage: 36
	},
	group2 : {
		name: "800 ms < t < 1200 ms",
		count: 583,
		percentage: 62
	},
	group3 : {
		name: "t > 1200 ms",
		count: 6,
		percentage: 0
	},
	group4 : {
		name: "failed",
		count: 0,
		percentage: 0
	},
	meanNumberOfRequestsPerSecond: {
		total: "30",
		ok: "30",
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
