function getEmi() {
    const loanAmount = parseInt(document.getElementById("loan-amount").value)
    if (!isValidInput(loanAmount, "Loan amount must be an integer")) return
    const interestRate = parseFloat(document.getElementById("interest-rate").value)
    if (!isValidInput(interestRate, "Interest rate must be a number")) return
    const loanTerm = parseInt(document.getElementById("loan-term").value)
    if (!isValidInput(loanTerm, "Loan term must be an integer")) return
    const request = {
        email: document.getElementById("email").value,
        loan_amount: loanAmount,
        interest_rate: interestRate,
        loan_term: loanTerm
    }

    fetch(
            "http://localhost:8080/emi", {
                method: "POST",
                referrerPolicy: "origin",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(request)
            }
        )
        .then((response) => response.json())
        .then((json) => handleEmiResponse(json))
}

function isValidInput(value, message) {
    if(isNaN(value)) {
        displayValidationError(message)
        return false
    } else return true
}

function handleEmiResponse(json) {
    clearEmiAndError()
    if (json.status) displayError(json)
    else displayEmi(json)
}

function clearEmiAndError() {
    renderEmi("")
    renderError("")
}

function displayError(json) {
    if (json.status == 400 && json.detail) renderError(json.detail)
    else renderError("An unexpected error occurred")
}

function displayEmi(json) {
    renderEmi(Number(json.emi).toFixed(2) + " &euro;")
}

function displayValidationError(message) {
    renderEmi("")
    renderError(message)
}

function renderError(value) {
    document.getElementById("emi-error").innerHTML = value
}

function renderEmi(value) {
    document.getElementById("emi-display").innerHTML = value
}
