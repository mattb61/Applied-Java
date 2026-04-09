const get_all = async () => {
    const result = await fetch('/app/cars/all');
    const json = await result.json();
    document.getElementById("results").innerHTML = json.reduce((acc, cur) => {
        return acc +
        `<p>make: ${cur.make}</p>` +
        `<p>model: ${cur.model}</p>` +
        `<p>year: ${cur.year}</p>` +
        "<br/>";
    }, "");
}

const get_by_make = async () => {
    const make = document.getElementById("get_by_make").value;
    const result = await fetch(`/app/cars/${make}`);
    const json = await result.json();
    document.getElementById("results").innerHTML = 
    `<h1>${make}</h1><br/>` + 
    json.reduce((acc, cur) => {
        return acc +
        `<p>model: ${cur.model}</p>` +
        `<p>year: ${cur.year}</p>` +
        "<br/>";
    }, "");
}

const get_by_make_and_model = async () => {
    const make = document.getElementById("get_by_make_and_model_make").value;
    const model = document.getElementById("get_by_make_and_model_model").value;
    const result = await fetch(`/app/cars/${make}/${model}`);
    const json = await result.json();
    document.getElementById("results").innerHTML = 
    `<h1>${make} ${model}</h1><br/>` + 
    json.reduce((acc, cur) => {
        return acc +
        `<p>year: ${cur.year}</p>` +
        "<br/>";
    }, "");
}

const get_by_year = async () => {
    const year = document.getElementById("get_by_year").value;
    const result = await fetch(`/app/cars/yearof/${year}`);
    const json = await result.json();
    document.getElementById("results").innerHTML = 
    `<h1>${year}</h1><br/>` + 
    json.reduce((acc, cur) => {
        return acc +
        `<p>make: ${cur.make}</p>` +
        `<p>model: ${cur.model}</p>` +
        "<br/>";
    }, "");
}

const post_car = async () => {
    const make = document.getElementById("post_make").value;
    const model = document.getElementById("post_model").value;
    const year = document.getElementById("post_year").value;
    const response = await fetch(`/app/cars`, 
        {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                make: make,
                model: model,
                year: +year
            })
        }
    );
    const message = await response.text();
    document.getElementById("results").innerHTML = `<p>${message}</p>`;
}

document.addEventListener("DOMContentLoaded", () => {
    document.getElementById("get_all_button").addEventListener("click", (event) => {
        event.preventDefault();
        get_all();
    });
    document.getElementById("get_by_make_button").addEventListener("click", (event) => {
        event.preventDefault();
        get_by_make();
    });
    document.getElementById("get_by_make_and_model_button").addEventListener("click", (event) => {
        event.preventDefault();
        get_by_make_and_model();
    });
    document.getElementById("get_by_year_button").addEventListener("click", (event) => {
        event.preventDefault();
        get_by_year();
    });
    document.getElementById("post_button").addEventListener("click", (event) => {
        event.preventDefault();
        post_car();
    });
    
});