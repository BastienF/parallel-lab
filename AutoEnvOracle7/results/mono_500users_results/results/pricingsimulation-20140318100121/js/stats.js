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
		total: "1305",
		ok: "1269",
		ko: "36"
	},
	minResponseTime : {
		total: "0",
		ok: "1730",
		ko: "0"
	},
	maxResponseTime : {
		total: "60020",
		ok: "39040",
		ko: "60020"
	},
	meanResponseTime : {
		total: "13003",
		ok: "12919",
		ko: "15958"
	},
	standardDeviation : {
		total: "9688",
		ok: "10367",
		ko: "0"
	},
	percentiles1 : {
		total: "38450",
		ok: "38230",
		ko: "60020"
	},
	percentiles2 : {
		total: "39010",
		ok: "38970",
		ko: "60020"
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
		count: 1269,
		percentage: 97
	},
	group4 : {
		name: "failed",
		count: 36,
		percentage: 2
	},
	meanNumberOfRequestsPerSecond: {
		total: "21",
		ok: "21",
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
		total: "1305",
		ok: "1269",
		ko: "36"
	},
	minResponseTime : {
		total: "0",
		ok: "1730",
		ko: "0"
	},
	maxResponseTime : {
		total: "60020",
		ok: "39040",
		ko: "60020"
	},
	meanResponseTime : {
		total: "13003",
		ok: "12919",
		ko: "15958"
	},
	standardDeviation : {
		total: "9688",
		ok: "10367",
		ko: "0"
	},
	percentiles1 : {
		total: "38450",
		ok: "38230",
		ko: "60020"
	},
	percentiles2 : {
		total: "39010",
		ok: "38970",
		ko: "60020"
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
		count: 1269,
		percentage: 97
	},
	group4 : {
		name: "failed",
		count: 36,
		percentage: 2
	},
	meanNumberOfRequestsPerSecond: {
		total: "21",
		ok: "21",
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
