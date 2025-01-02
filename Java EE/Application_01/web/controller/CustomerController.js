let customerDatabase = [];
loadCustomerDatabase();

$('#save-customer input, #save-customer textarea').on('input change', realTimeValidate);

$('#update-customer input, #update-customer textarea').on('input change', realTimeValidate);

$('#txt-search-valuec').on('input change', function () {
    const value = $(this).val();
    const option = $('#search-customer-by').val();

    const patterns = {
        'ID': /^C[0-9]{3,}$/,
        'Name': /^([A-Z]\.[A-Z]\. )?[A-Z][a-z]*( [A-Z][a-z]*)*$/,
    };

    if (patterns[option]) {
        realTimeValidateInput(value, patterns[option], this);
    }
});

$('#search-customer-by').on('change', function () {
    const value = $('#txt-search-valuec').val();
    const option = $(this).val();

    const patterns = {
        'ID': /^C[0-9]{3,}$/,
        'Name': /^([A-Z]\.[A-Z]\. )?[A-Z][a-z]*( [A-Z][a-z]*)*$/,
    };

    if (patterns[option]) {
        realTimeValidateInput(value, patterns[option], '#txt-search-valuec');
    }
});
$('#close-savec-btn, #close-savec-icon').on('click', function () {
    resetForm('#save-customer', '#save-customer input, #save-customer textarea');
    initializeNextCustomerId();
});

$('#close-updatec-btn, #close-updatec-icon').on('click', function () {
    resetForm('#update-customer', '#update-customer input, #update-customer textarea');
});
function loadCustomerDatabase() {
    $.ajax({
        url: "http://localhost:8080/Application_01_war_exploded/customer",
        method: "GET",
        dataType : "json",
        success: function (response) {
            console.log("Success response:", response);
            customerDatabase = response;
            initializeNextCustomerId();
            initializeOrderComboBoxes();
            loadAllCustomers();
        },
        error: function (xhr, status, error) {
            console.error('Error loading customer database:');
            console.error('Status:', status);
            console.error('Error:', error);
            console.error('Response:', xhr.responseText);
            alert('Failed to load customer database. Check console for details.');
        }
    })
}

function initializeNextCustomerId() {
    const prevId = customerDatabase.length > 0 ? customerDatabase[customerDatabase.length - 1].id : 'C000';
    const nextId = generateNextID(prevId);
    $('#txt-save-cid').val(nextId);
    $('#txt-save-cid').removeClass('is-invalid').addClass('is-valid');
}
function generateNextID(currentID) {
    if (!currentID) return 'C001';
    let number = parseInt(currentID.slice(1)) + 1;
    return `C${String(number).padStart(3, '0')}`;
}

function getCustomerById(id) {
    return customerDatabase.find(c => c.id === id);
}

function initializeOrderComboBoxes() {
    const customerOptions = customerDatabase.map(c => `<option value="${c.id}">${c.name}</option>`).join('');

    // Add these lines only if you have these elements in your HTML
    if($('#cmb-save-customer')) {
        $('#cmb-save-customer').html(customerOptions);
    }
    if($('#cmb-update-customer')) {
        $('#cmb-update-customer').html(customerOptions);
    }
}

function realTimeValidate() {
    const element = $(this);
    const pattern = new RegExp(element.attr('pattern'));
    const value = element.val();

    if (pattern.test(value)) {
        element.removeClass('is-invalid').addClass('is-valid');
    } else {
        element.removeClass('is-valid').addClass('is-invalid');
    }
}

function realTimeValidateInput(value, pattern, element) {
    if (pattern.test(value)) {
        $(element).removeClass('is-invalid').addClass('is-valid');
    } else {
        $(element).removeClass('is-valid').addClass('is-invalid');
    }
}

function resetForm(formId, elements) {
    $(formId).trigger('reset');
    $(elements).removeClass('is-valid is-invalid');
}

function getCustomerByName(name) {
    return customerDatabase.filter(c => c.name === name);
}

function appendToCustomerTable(customer) {
    $('#customer-tbody').append(`
        <tr>
            <td>${customer.id}</td>
            <td>${customer.name}</td>
            <td>${customer.address}</td>
        </tr>
    `);
}

function getCustomerByOption(option, value) {
    if (option === 'ID') return getCustomerById(value);
    if (option === 'Name') return getCustomerByName(value);
    return null;
}

function loadAllCustomers() {
    $('#customer-tbody').empty();
    for (const customer of customerDatabase) {
        appendToCustomerTable(customer);
    }
}

function sortCustomerDatabaseById() {
    customerDatabase.sort(function(a, b) {
        const idA = parseInt(a.id.replace('C', ''), 10);  // Remove 'C' and parse as integer
        const idB = parseInt(b.id.replace('C', ''), 10);  // Remove 'C' and parse as integer

        return idA - idB;  // Sort in ascending order
    });
}

$('#save-customer').on('submit', function (event) {
    event.preventDefault();

    let isValidated = $('#save-customer input, #save-customer textarea').toArray().every(element => $(element).hasClass('is-valid'));

    if (isValidated) {
        const id = $('#txt-save-cid').val();
        const name = $('#txt-save-cname').val();
        const address = $('#txt-save-caddress').val();

        if (!customerDatabase.some(c => c.id === id)) {
            $.ajax({
                url: "http://localhost:8080/Application_01_war_exploded/customer",
                method: "POST",
                data: {
                    id: id,
                    name: name,
                    address: address
                },

                success: function (response) {
                    showToast('success', 'Customer saved successfully !');
                    resetForm('#save-customer', '#save-customer input, #save-customer textarea');
                    loadCustomerDatabase();
                    sortCustomerDatabaseById();
                    initializeOrderComboBoxes();
                },
                error: function (error) {
                    console.log(error)
                }
            });
        } else {
            showToast('error', 'Customer ID already exists !');
        }
    }
});

$('#txt-update-cid').on('input', function (event) {
    if (customerDatabase.some(c => c.id === $(this).val())) {
        const customer = getCustomerById($(this).val());
        $('#txt-update-cname').val(customer.name);
        $('#txt-update-caddress').val(customer.address);
        $('#update-customer input, #update-customer textarea').addClass('is-valid').removeClass('is-invalid');
    } else {
        $('#txt-update-cname').val('');
        $('#txt-update-caddress').val('');
        $('#txt-update-cno').val('');
        $('#update-customer input, #update-customer textarea').removeClass('is-valid');
    }
});

$('#update-customer').on('submit', function (event) {
    event.preventDefault();

    let isValidated = $('#update-customer input, #update-customer textarea').toArray().every(element => $(element).hasClass('is-valid'));

    if (isValidated) {
        const id = $('#txt-update-cid').val();
        const name = $('#txt-update-cname').val();
        const address = $('#txt-update-caddress').val();

        if (customerDatabase.some(c => c.id === id)) {
            $.ajax({
                url: "http://localhost:8080/Application_01_war_exploded/customer?id="+ id + "&name=" + name  + "&address=" + address,
                method: "PUT",
                success: function (response) {
                    showToast('success', 'Customer updated successfully !');
                    resetForm('#update-customer', '#update-customer input, #update-customer textarea');
                    loadCustomerDatabase();
                },
                error: function (error) {
                    console.log(error)
                }
            });
        } else {
            showToast('error', 'Customer ID not found !');
        }
    }
});

$('#search-customer').on('submit', function (event) {
    event.preventDefault();

    let isValidated = $('#txt-search-valuec').hasClass('is-valid');

    if (isValidated) {
        const value = $('#txt-search-valuec').val();
        const option = $('#search-customer-by').val();

        // Clear the table first
        $('#customer-tbody').empty();
        
        const customers = getCustomerByOption(option, value);

        if (Array.isArray(customers) && customers.length > 0) {
            for (const customer of customers) {
                appendToCustomerTable(customer);
            }
            showToast('success', 'Customer search completed successfully !');
        } else if (customers && !Array.isArray(customers)) {
            appendToCustomerTable(customers);
            showToast('success', 'Customer search completed successfully !');
        } else {
            showToast('error', `Customer ${option} not found !`);
        }
    }
});

$('#clear-customer-btn').on('click', function () {
    $('#txt-search-valuec').val('');
    $('#txt-search-valuec').removeClass('is-invalid is-valid');
    $('#txt-search-valuec').next().hide();
    $('#search-customer-by').val('ID');
    $('#customer-tbody').empty();
    showToast('success', 'Customer page cleared !');
});

$('#delete-customer-btn').on('click', function () {
    const value = $('#txt-search-valuec').val();
    const option = $('#search-customer-by').val();

    const customers = getCustomerByOption(option, value);

    if (Array.isArray(customers) && customers.length > 0) {
        $('#confirm-delete-model .modal-body').text('Are you sure you want to delete this customer ?');
        $('#confirm-delete-model').modal('show');

        $('#confirm-delete-btn').one('click', function () {
            for (const customer of customers) {
                $.ajax({
                    url: "http://localhost:8080/Application_01_war_exploded/customer?id="+ customer.id,
                    method: 'DELETE',
                    success: function (response) {
                        showToast('success', 'Customer deleted successfully!');
                        $('#confirm-delete-model').modal('hide');  // Hide modal after deletion
                        loadCustomerDatabase();
                        initializeOrderComboBoxes();
                    },
                    error: function (error) {
                        console.log(error)
                    }
                })
            }
        });
    } else if (customers && !Array.isArray(customers)) {
        $('#confirm-delete-model .modal-body').text('Are you sure you want to delete this customer ?');
        $('#confirm-delete-model').modal('show');

        $('#confirm-delete-btn').one('click', function () {
            $.ajax({
                url: "http://localhost:8080/Application_01_war_exploded/customer?id="+ customers.id,
                method: 'DELETE',
                success: function (response) {
                    showToast('success', 'Customer deleted successfully!');
                    $('#confirm-delete-model').modal('hide');  // Hide modal after deletion
                    loadCustomerDatabase();
                    initializeOrderComboBoxes();
                },
                error: function (error) {
                    console.log(error)
                }
            })
        });
    } else {
        showToast('error', `Customer ${option} not found!`);
        loadAllCustomers();
    }
});

$('#load-all-customer-btn').on('click', function () {
    loadAllCustomers();
    showToast('success', 'All customers loaded successfully !');
});