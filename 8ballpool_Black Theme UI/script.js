
      const userId = localStorage.getItem("userData")
        ? JSON.parse(localStorage.getItem("userData"))
        : null;

      console.log(userId);
      if (userId === null) {
        window.location.href = "login/login.html";
      } else {
        if (userId.userType == "admin") {
          const af = document.getElementById("adminAsidefuntions");
          const uf = document.getElementById("userAsidefuntions");
          af.classList.remove("hide");
          uf.classList.add("hide");
        } else {
          const af = document.getElementById("adminAsidefuntions");
          const uf = document.getElementById("userAsidefuntions");
          af.classList.add("hide");
          uf.classList.remove("hide");
        }

        let customerData = [];

        async function fetchTables() {
          try {
            const response = await fetch("http://localhost:8080/club/tables");
            const tables = await response.json();

            // Clear existing boxes
            const boxesContainer = document.getElementById(
              "dynamicBoxesContainer"
            );
            boxesContainer.innerHTML = "";

            // Create a box for each table
            tables.forEach((table) => {
              const tableBox = document.createElement("div");
              tableBox.className = "tableBox";

              const tableBorder = document.createElement("div");
              tableBorder.className = "tableBorder";

              const hole1 = document.createElement("div");
              hole1.className = "hole";
              hole1.style.top = "10px";
              hole1.style.left = "10px";

              const hole2 = document.createElement("div");
              hole2.className = "hole";
              hole2.style.top = "10px";
              hole2.style.right = "10px";

              const hole3 = document.createElement("div");
              hole3.className = "hole";
              hole3.style.bottom = "10px";
              hole3.style.left = "10px";

              const hole4 = document.createElement("div");
              hole4.className = "hole";
              hole4.style.bottom = "10px";
              hole4.style.right = "10px";

              const tableIdLabel = document.createElement("label");
              tableIdLabel.classList.add("tableIdLabel");
              tableIdLabel.textContent = `Table ID: ${table.tableId}`;

              const tableList = document.createElement("ul");
              tableList.className = "table-list";
              const listItem = document.createElement("li");
              listItem.className = "table-list-item";
              listItem.textContent = `${table.tableInfo}`;
              tableList.appendChild(listItem);

              const tablePrice = document.createElement("label");
              tablePrice.classList.add("tablePrice");
              tablePrice.textContent = `Rs. ${table.pricePerHours}/H`;

              const tableNameLabel = document.createElement("label");
              tableNameLabel.textContent = "Customer Name:";

              const tableNameInput = document.createElement("input");
              tableNameInput.type = "text";

              const startButton = document.createElement("button");
              startButton.textContent = "Start";
              startButton.addEventListener("click", () =>
                startTimer(tableBox, startButton, stopButton)
              );

              const stopButton = document.createElement("button");
              stopButton.textContent = "Stop";
              stopButton.disabled = true; // Initially disabled
              stopButton.addEventListener("click", () =>
                stopTimer(tableBox, startButton, stopButton, table.tableId)
              );

              const paymentMethodLabel = document.createElement("label");
              paymentMethodLabel.textContent = "Payment Method:";

              const paymentMethodSelect = document.createElement("select");

              const select = document.createElement("option");
              select.value = "select";
              select.textContent = "select";

              const paytmOption = document.createElement("option");
              paytmOption.value = "paytm";
              paytmOption.textContent = "Paytm";

              const cashOption = document.createElement("option");
              cashOption.value = "cash";
              cashOption.textContent = "Cash";

              const debtOption = document.createElement("option");
              debtOption.value = "due";
              debtOption.textContent = "Due/Table Transfer";

              const extendOption = document.createElement("option");
              extendOption.value = "extend";
              extendOption.textContent = "extend";

              paymentMethodSelect.appendChild(select);
              paymentMethodSelect.appendChild(paytmOption);
              paymentMethodSelect.appendChild(cashOption);
              paymentMethodSelect.appendChild(debtOption);

              const timer = document.createElement("div");
              timer.className = "timer";
              timer.textContent = "00:00:00";

              const searchBox = document.createElement("input");
              searchBox.type = "text";
              searchBox.placeholder = "Search Customer";
              searchBox.id = "searchBox" + table.tableId;
              searchBox.addEventListener("input", () => {
                const searchTerm = searchBox.value;
                filterCustomerData(searchTerm, table.tableId);
                logFilteredData(searchTerm, table.tableId);
              });

              const searchResultsContainer = document.createElement("div");
              searchResultsContainer.className = "search-results-container";
              searchResultsContainer.setAttribute(
                "data-table-id",
                table.tableId
              ); // Set data attribute for tableId

              const customerName = document.createElement("div");
              customerName.className = "customerName";
              customerName.id = "customerName" + table.tableId;
              // Append child elements to tableBox

              tableBox.appendChild(tableIdLabel);
              tableBox.appendChild(tableList);
              tableBox.appendChild(searchBox);
              tableBox.appendChild(searchResultsContainer);
              tableBox.appendChild(customerName);
              // tableBox.appendChild(tableNameLabel);
              // tableBox.appendChild(tableNameInput);
              tableBox.appendChild(paymentMethodLabel);
              tableBox.appendChild(paymentMethodSelect);
              tableBox.appendChild(tablePrice);
              tableBox.appendChild(timer);
              tableBox.appendChild(startButton);
              tableBox.appendChild(stopButton);
              tableBox.appendChild(tableBorder);

              // Append the tableBox to the tableBoxContainer
              boxesContainer.appendChild(tableBox);
            });
          } catch (error) {
            console.error("Error fetching tables:", error.message);
          }
        }

        fetchCustomerData();

        async function fetchCustomerData() {
          try {
            const response = await fetch(
              "http://localhost:8080/user/getCustomer"
            );
            const data = await response.json();

            customerData = data;
            console.log(customerData);
          } catch (error) {
            console.error("Error fetching customer data:", error);
          }
        }

        function filterCustomerData(searchTerm, tableId) {
          let st = searchTerm;
          const filteredCustomers = customerData.filter((customer) => {
            const fullName =
              `${customer.customerName} (${customer.customerMobileNo})`.toLowerCase();
            return fullName.includes(searchTerm.toLowerCase());
          });

          const searchResultsContainer = document.querySelector(
            `.search-results-container[data-table-id="${tableId}"]`
          );
          searchResultsContainer.innerHTML = "";
          // Check if the element exists before modifying its content
          if (searchResultsContainer && st.length != 0) {
            searchResultsContainer.innerHTML = ""; // Clear previous search results

            filteredCustomers.forEach((customer) => {
              const resultItem = document.createElement("div");
              resultItem.textContent = `${customer.customerName} - ${customer.customerMobileNo}`;
              resultItem.addEventListener("click", () =>
                selectCustomer(customer, tableId, searchResultsContainer)
              );
              searchResultsContainer.appendChild(resultItem);
            });
          } else {
            console.error(
              `Search results container not found for table ${tableId}`
            );
          }
        }

        function selectCustomer(customer, tableId, searchResultsContainer) {
          const scb = document.getElementById("searchBox" + tableId);
          const scn = document.getElementById("customerName" + tableId);
          searchResultsContainer.innerHTML = "";
          scn.innerHTML = `<p>Customer Name : ${customer.customerName}</p>`;
          scb.value = customer.customerMobileNo;
          console.log(customer);
        }

        function logFilteredData(searchTerm, tableId) {
          const filteredCustomers = customerData.filter((customer) => {
            const fullName =
              `${customer.customerName} (${customer.customerMobileNo})`.toLowerCase();
            return fullName.includes(searchTerm.toLowerCase());
          });

          console.log(`Filtered data for table ${tableId}:`, filteredCustomers);
        }

        fetchTables();

        function viewReport() {
          window.open("./report.html", "_blank");
        }

        function viewDues() {
          window.open("./dues.html", "_blank");
        }

        function viewExtendedBill() {
          window.open("./extended_bill.html", "_blank");
        }

        function startTimer(box, startButton, stopButton, clearButton) {
          const timer = box.querySelector(".timer");
          const tableNameInput = box.querySelector('input[type="text"]');
          const customerName = tableNameInput.value;
          const tableId = extractTableId(box.querySelector("label")); // Extract table ID from label

          let interval;
          let second = 0;
          let hours = 0;
          let minutes = 0;

          if (customerName.trim() === "") {
            alert("Customer name cannot be empty.");
            return;
          }
          const timerData = {
            customerName: customerName,
            tableId: tableId,
            startTime: Date.now(), // Store the start time
          };
          localStorage.setItem("timerData", JSON.stringify(timerData));

          interval = setInterval(function () {
            second++; // Increment the total seconds
            hours = Math.floor(second / 3600);
            minutes = Math.floor((second % 3600) / 60);
            let seconds = second % 60;

            // Add leading zeros if necessary
            hours = hours < 10 ? "0" + hours : hours;
            minutes = minutes < 10 ? "0" + minutes : minutes;
            seconds = seconds < 10 ? "0" + seconds : seconds;

            timer.textContent = hours + ":" + minutes + ":" + seconds;
          }, 1000);

          // Save the timer interval reference, customer name, and buttons to the box for later use
          box.timerInterval = interval;
          box.customerName = customerName;
          box.tableId = tableId; // Save table ID
          box.startButton = startButton;
          box.stopButton = stopButton;

          // Enable Stop button, disable Start button, and hide Clear button
          stopButton.disabled = false;
          startButton.disabled = true;
          clearButton.style.display = "none";

          // Add 'active' class to the tableBox
          box.classList.add("active");
        }

        function stopTimer(box, startButton, stopButton, tableId) {
          const timer = box.querySelector(".timer");
          const tableNameInput = box.querySelector('input[type="text"]');
          const paymentMethodSelect = box.querySelector("select");
          const selectedBillingMode = paymentMethodSelect.value;
          const scn = document.getElementById("customerName" + tableId);

          if (box.querySelector("select").value === "select") {
            alert("Please select payment method");
          } else {
            // Clear the text field value
            tableNameInput.value = "";
            scn.innerHTML = "";
            clearInterval(box.timerInterval);

            // Convert total seconds into minutes
            const total_time = Math.floor(
              timer.textContent
                .split(":")
                .reduce(
                  (acc, val, idx) => acc + val * Math.pow(60, 2 - idx),
                  0
                ) / 60
            );
            console.log("Total Time (minutes):", total_time);

            // Save the information to the server and get billing details
            saveinfo(
              total_time,
              box.customerName,
              box.tableId,
              selectedBillingMode
            ).then((billingDetails) => {
              showPopup(billingDetails, box);
            });

            // Enable Start button, disable Stop button, and show Clear button
            startButton.disabled = false;
            stopButton.disabled = true;

            // Remove the 'active' class from the tableBox
            box.classList.remove("active");
          }
        }

        function extractTableId(labelElement) {
          const labelText = labelElement.textContent;
          const match = labelText.match(/Table ID: (\d+)/);
          return match ? match[1] : null;
        }

        function showPopup(billingDetails, box) {
          const popup = document.getElementById("popup");
          const popupContent = document.getElementById("popupContent");

          const formattedDate = new Date(billingDetails.date).toLocaleString();

          const popupHTML = `
            <p>Billing Information:</p>
            <p>Customer Name: ${billingDetails.customerName}</p>
            <p>Total Time: ${billingDetails.totalTimePlayed} minutes</p>
            <p><strong>Bill Amount: ${billingDetails.billAmount} rs</strong></p>
            <p>Billing By: ${billingDetails.billingBy}</p>
            <p>Billing Mode: ${billingDetails.billingMode}</p>
            <p id="billingDetailsStatus"> ${billingDetails.status}</p>

            <button class="close-btn" onclick="closePopup('${box.id}')">Close</button>
        `;

          popupContent.innerHTML = popupHTML;
          popup.style.display = "flex";
          const bds = document.getElementById("billingDetailsStatus");
          if (
            billingDetails.status == "Bill Created" ||
            billingDetails.status == "Dues updated"
          ) {
            bds.style.backgroundColor = "Green";
          } else {
            bds.style.backgroundColor = "Red";
          }
        }

        function closePopup(boxId) {
          const popup = document.getElementById("popup");
          const popupContent = document.getElementById("popupContent");
          popupContent.innerHTML = "";
          popup.style.display = "none";

          const box = document.getElementById(boxId);
          const timer = box.querySelector(".timer");
          const tableNameInput = box.querySelector('input[type="text"]');

          // Stop the timer if it is running
          clearInterval(box.timerInterval);

          // Reset the timer and clear the text field
          timer.textContent = "00:00:00";
          tableNameInput.value = "";

          box.startButton.disabled = false;
          box.stopButton.disabled = true;
          box.classList.remove("active");
        }

        function manualBillingBox() {
          const popup = document.getElementById("popup");
          const popupContent = document.getElementById("popupContent");

          const popupHTML = `
            <h1>Create Bill</h1>
            <input type="text" id="createBillUserId" placeholder="Customer ID"/>
            <input type="number" id="createBillAmount" placeholder="Amount"/>
            <select id="manualBillingPaymentMethod">
          <option value="select">Select Payment Method</option>
          <option value="paytm">Paytm</option>
          <option value="cash">cash</option>
          <option value="due">dues</option>
        </select>
            <input type="text" id="createBillDescription" placeholder="Description"/>
            <Button onclick="manualBillingFunction()">Add</Button>
            <Button class="close-btn" onclick={popup.style.display="none";}>Close</Button>

            `;
          popupContent.innerHTML = popupHTML;
          popup.style.display = "flex";
        }

        function manualBillingFunction() {
          // Retrieve values from the form
          var paymentMode = document.getElementById(
            "manualBillingPaymentMethod"
          ).value;
          var customerNumber =
            document.getElementById("createBillUserId").value;
          var amount = document.getElementById("createBillAmount").value;
          var description = document.getElementById(
            "createBillDescription"
          ).value;
          const userId = localStorage.getItem("userData")
            ? JSON.parse(localStorage.getItem("userData")).userId
            : null;

          const requestBody = `customerNumber=${customerNumber}&userId=${userId}&description=${description}&amount=${amount}&paymentMode=${paymentMode}`;

          // Assuming you are using fetch to send a POST request to the server
          fetch("http://localhost:8080/user/payBillManual", {
            method: "POST",
            headers: {
              "Content-Type": "application/x-www-form-urlencoded",
            },
            body: requestBody,
          })
            .then((response) => response.text())
            .then((result) => {
              // Handle the result from the server
              alert(result);
              console.log(result);

              // You can add further logic based on the result if needed
            })
            .catch((error) => {
              console.error("Error:", error);
            });
        }

        function adduserBox() {
          const popup = document.getElementById("popup");
          const popupContent = document.getElementById("popupContent");

          const popupHTML = `
            <h1>Create User</h1>
            <input type="text" id="newUserId" placeholder="User ID"/>
            <input type="text" id="newUserName" placeholder="User Name"/>
            <select name="newUserType" id="newUserType">
              <option value="user">User</option>
              <option value="admin">Admin</option>
            </select>
            <input type="password" id="newUserPassword" placeholder="Password"/>


            <Button onclick="addUserFunction()">Add</Button>
            <Button class="close-btn" onclick={popup.style.display="none";}>Close</Button>

            `;
          popupContent.innerHTML = popupHTML;
          popup.style.display = "flex";
        }

        function addUserFunction() {
          const userId = document.getElementById("newUserId").value;
          const sname = document.getElementById("newUserName").value;
          const userType = document.getElementById("newUserType").value;
          const password = document.getElementById("newUserPassword").value;

          console.log(userType);

          const user = {
            userId: userId,
            sname: sname,
            userType: userType,
            password: password,
          };

          fetch("http://localhost:8080/user/validate/createAccount", {
            method: "POST",
            headers: {
              "Content-Type": "application/json",
            },
            body: JSON.stringify(user),
          })
            .then((response) => response.text())
            .then((data) => {
              alert(data + " User was added sucessfully ");
              console.log(data);
            })
            .catch((error) => {
              alert("Error!!");
              console.error("Error:", error);
            });
        }

        function addCustomerBox() {
          const popup = document.getElementById("popup");
          const popupContent = document.getElementById("popupContent");

          const popupHTML = `
            <h1>Add Customer</h1>
            <input type="ext" id="addCustomerName"  placeholder="Customer Name"/>
            <input type="number" min="0" maxlength="10" id="addCustomerNumber"  placeholder="Mobile Number"  />
            <input type="number" min="0" maxlength="10" id="maxCreditLimit"  placeholder="Credit Limit "  />

            <Button onclick="addCustomerFunction()">Add</Button>
            <Button class="close-btn" onclick={popup.style.display="none";}>Close</Button>

            `;
          popupContent.innerHTML = popupHTML;
          popup.style.display = "flex";
        }

        function addCustomerFunction() {
          var customerName = document.getElementById("addCustomerName").value;
          var customerMobileNo =
            document.getElementById("addCustomerNumber").value;

          var maxCreditLimit = document.getElementById("maxCreditLimit").value;

          var customer = {
            customerName: customerName,
            customerMobileNo: customerMobileNo,
            maxCreditLimit: maxCreditLimit,
          };

          // Make API request
          fetch("http://localhost:8080/user/addCustomer", {
            method: "POST",
            headers: {
              "Content-Type": "application/json",
            },
            body: JSON.stringify(customer),
          })
            .then((response) => response.text())
            .then((data) => {
              fetchCustomerData();
              alert(data); // You can handle the response accordingly
            })
            .catch((error) => {
              console.error("Error:", error);
              alert("Unable to add customer. Please try again later.");
            });
        }

        function addTableBox() {
          const popup = document.getElementById("popup");
          const popupContent = document.getElementById("popupContent");

          const popupHTML = `
            <h1>Add Table</h1>
            <input type="text" id="addTableTableInfo" name="TableInfo" placeholder="Table Info "/>
            <input type="number" id="addTablePrice" name="price" placeholder="Price Per Hour"/>
            <Button onclick="addTableFunction()">Add</Button>
            <Button class="close-btn" onclick={popup.style.display="none";}>Close</Button>

            `;
          popupContent.innerHTML = popupHTML;
          popup.style.display = "flex";
        }

        function addTableFunction() {
          var tableName = document.getElementById("addTableTableInfo").value;
          var price = document.getElementById("addTablePrice").value;

          popup.style.display = "none";

          // Construct the URL with query parameters
          var url = "http://localhost:8080/billing/admin/addTable";
          url += "?tableName=" + encodeURIComponent(tableName);
          url += "&price=" + encodeURIComponent(price);
          url += "&userId=" + encodeURIComponent(userId.userId);

          // Using FormData to send data without encoding
          var formData = new FormData();
          formData.append("tableName", tableName);
          formData.append("price", price);
          formData.append("userId", userId.userId);
          console.log(url);
          // Using the fetch function
          fetch(url, {
            method: "POST",
            // body: formData,
          })
            .then((response) => response.text())
            .then((data) => {
              // Handle the response from the server
              alert(data); // You can customize this based on your needs
              location.reload();
            })
            .catch((error) => {
              console.error("Error:", error);
            });
        }

        function deleteTableBox() {
          const popup = document.getElementById("popup");
          const popupContent = document.getElementById("popupContent");

          const popupHTML = `
            <h1>Delete Table</h1>
            <input type="number" id="DeleteTableId" name="price" placeholder="Enter Table ID"/>
            <Button onclick="DeleteTableFunction()">DELETE</Button>
            <Button class="close-btn" onclick={popup.style.display="none";}>Close</Button>
            `;
          popupContent.innerHTML = popupHTML;
          popup.style.display = "flex";
        }

        function DeleteTableFunction() {
          var tableId = document.getElementById("DeleteTableId").value;
          var url = "http://localhost:8080/billing/admin/deleteTable";
          url += "?tableId=" + encodeURIComponent(tableId);
          url += "&userId=" + encodeURIComponent(userId.userId);
          fetch(url, {
            method: "POST",
          })
            .then((response) => response.text())
            .then((data) => {
              alert(data);
              location.reload();
            })
            .catch((error) => {
              console.error("Error:", error);
            });
        }

        function viewRecentBillbox() {
          let recentBillsData = [];
          fetchRecentBills();

          // Function to fetch recent bills
          async function fetchRecentBills() {
            try {
              const response = await fetch(
                "http://localhost:8080/user/getRecentBillsInfo"
              );
              recentBillsData = await response.json();
              // console.log(recentBillsData); // Log for verification
              displayRecentBills();
            } catch (error) {
              console.error("Error fetching recent bills:", error.message);
            }
          }

          // Function to display recent bills
          function displayRecentBills() {
            const popupRecent = document.getElementById("popupRecent");
            const popupRecentContent =
              document.getElementById("popupRecentContent");

            // Check if the container exists
            if (!popupRecentContent) {
              console.error("Recent bills container not found.");
              return;
            }

            // Clear the existing content
            popupRecentContent.innerHTML = "";

            // Check if there are recent bills to display
            if (recentBillsData.length === 0) {
              popupRecentContent.textContent = "No recent bills available.";
              return;
            }

            // Create a table to display recent bills
            const table = document.createElement("table");
            table.border = "1";

            // Create table header
            const headerRow = table.insertRow(0);
            const headers = [
              "Bill No",
              "Description",
              "Customer Name",
              "Bill Amount",
              "Billing By",
              "Billing Mode",
            ];
            headers.forEach((headerText) => {
              const headerCell = document.createElement("th");
              headerCell.textContent = headerText;
              headerRow.appendChild(headerCell);
            });

            // Populate the table with recent bills data
            recentBillsData.forEach((bill) => {
              const row = table.insertRow();
              const values = [
                bill.billNo,
                bill.description,
                bill.customerName,
                bill.billAmount,
                bill.billingBy,
                bill.billingMode,
              ];
              values.forEach((value) => {
                const cell = row.insertCell();
                cell.textContent = value;
              });
            });

            // Append the table to the container
            popupRecentContent.innerHTML += `<h1>RECENT BILLS</h1>`;
            popupRecentContent.appendChild(table);
            popupRecentContent.innerHTML += `<Button class="close-btn" onclick={popupRecent.style.display="none";}>Close</Button>`;

            // Example: Open the popup
            if (popupRecent) {
              popupRecent.style.display = "block";
            }
          }
        }

        function saveinfo(minutesPlayed, customerName, tableId, paymentMode) {
          const url = "http://localhost:8080/bill/save";
          console.log(minutesPlayed);
          const userDataString = localStorage.getItem("userData");
          const userData = userDataString ? JSON.parse(userDataString) : null;

          const data = {
            tableNo: tableId,
            minutes: minutesPlayed,
            customerName: customerName,
            cashierUserId: userData ? userData.userId : null,
            billingMode: paymentMode,
          };

          return fetch(url, {
            method: "POST",
            headers: {
              "Content-Type": "application/json",
            },
            body: JSON.stringify(data),
          })
            .then((response) => response.json())
            .then((data) => {
              console.log("Success:", data);
              return data;
            });
        }
        function retrieveTimerData() {
          const timerDataString = localStorage.getItem("timerData");
          return timerDataString ? JSON.parse(timerDataString) : null;
        }

        document.addEventListener("DOMContentLoaded", function (event) {
          const timerData = retrieveTimerData();
          if (timerData) {
            // Resume the timer if there's existing timer data
            const box = document.querySelector(
              `.tableBox[data-table-id="${timerData.tableId}"]`
            );
            if (box) {
              const startButton = box.querySelector("button");
              const stopButton = box.querySelector("button + button");
              startTimer(box, startButton, stopButton);
            } else {
              // If the table associated with the timer data is not found, clear the timer data
              localStorage.removeItem("timerData");
            }
          }
        });
        function clearTable(box, startButton, stopButton) {
          const timer = box.querySelector(".timer");
          const tableNameInput = box.querySelector('input[type="text"]');
          timer.textContent = "00:00:00";
          tableNameInput.value = "";

          startButton.disabled = false;
          stopButton.disabled = true;
          clearButton.style.display = "none";

          box.classList.remove("active");
        }

        document.addEventListener("DOMContentLoaded", function () {
          // Retrieve user data from localStorage
          const userDataString = localStorage.getItem("userData");
          const userData = userDataString ? JSON.parse(userDataString) : null;
          console.log(userData);
          // Display user data on the page
          if (userData) {
            const userDataContainer =
              document.getElementById("userDataContainer");

            const userDataHTML = `

      <p>Welcome: ${userData.sname}</p>
        `;

            userDataContainer.innerHTML = userDataHTML;
          }
        });

        // Function to fetch today's collection details
        async function fetchTodaysCollectionDetails() {
          try {
            const userId = localStorage.getItem("userData")
              ? JSON.parse(localStorage.getItem("userData")).userId
              : null;

            if (userId) {
              const response = await fetch(
                `http://localhost:8080/billing/getTodayBillAmount?cashierId=${userId}`
              );
              const collectionDetails = await response.json();

              // Display today's collection details on the page
              const collectionDetailsContainer = document.getElementById(
                "collectionDetailsContainer"
              );
              const collectionDetailsHTML = `<div class="asideDetails"><p>Cash Collection</p>
        <p> Rs ${
          collectionDetails.todayCashCollection === undefined
            ? 0
            : collectionDetails.todayCashCollection
        }</p></div>
        <div class="asideDetails">
        <p>Paytm Collection</p>
        <p> Rs ${
          collectionDetails.todayPaytmCollection === undefined
            ? 0
            : collectionDetails.todayPaytmCollection
        }</p>
        </div>

        <div class="asideDetails">
        <p>Dues</p>
        <p> Rs ${
          collectionDetails.dueCustomerBill === undefined
            ? 0
            : collectionDetails.dueCustomerBill
        }</p>
        </div>






        <div class="asideDetails">
        <p>Total Collection</p>
        <p> Rs ${
          collectionDetails.totalCollection === undefined
            ? 0
            : collectionDetails.totalCollection
        }</p>
        </div>

      `;
              collectionDetailsContainer.innerHTML = collectionDetailsHTML;
            }
          } catch (error) {
            console.error(
              "Error fetching today's collection details:",
              error.message
            );
          }
        }

        function Logout() {
          localStorage.clear();
          location.reload();
        }

        // Function to refresh collection details
        function refreshCollectionDetails() {
          fetchTodaysCollectionDetails();
        }

        // Call the fetchTodaysCollectionDetails function when the page is loaded
        document.addEventListener("DOMContentLoaded", function () {
          fetchTodaysCollectionDetails();

          // Add event listeners to trigger collection details refresh
          document.addEventListener("click", refreshCollectionDetails);
          // Add more event listeners as needed
        });
      }
