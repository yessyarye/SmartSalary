document.addEventListener("DOMContentLoaded", () => {
  const table = document.getElementById("dataTable");
  const rows = Array.from(table.getElementsByTagName("tr")).slice(1); // Ambil baris data tanpa header
  const searchInput = document.getElementById("search");
  const prevBtn = document.getElementById("prevBtn");
  const nextBtn = document.getElementById("nextBtn");
  const pageNumber = document.getElementById("pageNumber");

  const rowsPerPage = 10; // Jumlah data per halaman
  let currentPage = 1;
  let filteredRows = rows;

  // Menampilkan data pada halaman tertentu
  function displayPage(page) {
    const startIndex = (page - 1) * rowsPerPage;
    const endIndex = startIndex + rowsPerPage;

    // Sembunyikan semua baris
    filteredRows.forEach((row, index) => {
      if (index >= startIndex && index < endIndex) {
        row.style.display = "";
      } else {
        row.style.display = "none";
      }
    });

    // Perbarui nomor halaman
    pageNumber.textContent = page;

    // Nonaktifkan tombol prev dan next jika diperlukan
    prevBtn.disabled = page === 1;
    nextBtn.disabled = endIndex >= filteredRows.length;
  }

  function searchTable() {
    const filter = searchInput.value.toLowerCase();
    let matchedCount = 0;

    for (let i = 1; i < rows.length; i++) {
      const rowText = rows[i].textContent.toLowerCase();
      if (rowText.includes(filter)) {
        rows[i].style.display = "";
        matchedCount++;
      } else {
        rows[i].style.display = "none";
      }
    }

    if (!filter) {
      paginateTable(10);
    }
  }

  searchInput.addEventListener("input", searchTable);

  // Fungsi untuk mengubah halaman
  function changePage(direction) {
    currentPage += direction;
    displayPage(currentPage);
  }

  // Event listener untuk input pencarian
  searchInput.addEventListener("input", searchTable);

  // Event listener untuk tombol navigasi
  prevBtn.addEventListener("click", () => changePage(-1));
  nextBtn.addEventListener("click", () => changePage(1));

  // Menampilkan halaman pertama saat awal
  displayPage(currentPage);
});

// Toggle password visibility
document.querySelectorAll(".toggle-icon").forEach((icon) => {
  icon.addEventListener("click", () => {
    const input = icon.previousElementSibling;
    const isPassword = input.type === "password";
    input.type = isPassword ? "text" : "password";
    icon.classList.toggle("fa-eye");
    icon.classList.toggle("fa-eye-slash");
  });
});

// Check email availability using AJAX when the email input loses focus
document.getElementById("email").addEventListener("blur", function () {
  const email = this.value;
  fetch(`/check-email?email=${email}`)
    .then((response) => response.json())
    .then((data) => {
      const errorMsg = document.getElementById("email-error");
      if (data.exists) {
        errorMsg.style.display = "block"; // Show error message if email exists
      } else {
        errorMsg.style.display = "none"; // Hide error message if email is available
      }
    })
    .catch((error) => console.error("Error:", error));
});

// Show warning when password and confirm password don't match
function validatePasswordMatch() {
  let password = document.getElementById("password").value;
  let confirmPassword = document.getElementById("confirm-password").value;
  const passwordWarning = document.getElementById("password-warning");

  if (password !== confirmPassword) {
    passwordWarning.style.display = "block"; // Show warning if passwords don't match
  } else {
    passwordWarning.style.display = "none"; // Hide warning if passwords match
  }
}

// Add event listeners for real-time password matching validation
document
  .getElementById("password")
  .addEventListener("input", validatePasswordMatch);
document
  .getElementById("confirm-password")
  .addEventListener("input", validatePasswordMatch);

// Validate password match on form submit
document.querySelector("form").addEventListener("submit", function (event) {
  let password = document.getElementById("password").value;
  let confirmPassword = document.getElementById("confirm-password").value;

  // If passwords don't match, show alert and prevent form submission
  if (password !== confirmPassword) {
    event.preventDefault();
    alert("Password dan konfirmasi password harus sama.");
  }

  // Disable the form submission if email is already taken
  const emailError = document.getElementById("email-error").style.display;
  if (emailError === "block") {
    event.preventDefault();
    alert("Harap perbaiki email sebelum melanjutkan.");
  }
});

document.addEventListener("DOMContentLoaded", function () {
  const header = document.getElementById("main-header");

  // Fungsi untuk mengubah kelas saat scroll
  const toggleHeaderBackground = () => {
    if (window.scrollY > 50) {
      header.classList.add("scrolled");
      header.classList.remove("transparent");
    } else {
      header.classList.add("transparent");
      header.classList.remove("scrolled");
    }
  };

  // Event listener untuk scroll
  window.addEventListener("scroll", toggleHeaderBackground);
});

function toggleMenu() {
        var menu = document.getElementById("menu");
        var menuIcon = document.getElementById("menu-icon");

        if (menu.style.left === "-250px" || !menu.style.left) {
            menu.style.left = "0";
            menuIcon.innerHTML = '<i class="fa-solid fa-xmark"></i>'; // Ganti ikon menjadi "X"
        } else {
            menu.style.left = "-250px";
            menuIcon.innerHTML = "&#9776;";
        }
    }

// Fungsi untuk toggle submenu
function toggleSubmenu(event) {
    event.preventDefault(); // Mencegah pengalihan ke halaman lain saat submenu diklik
    var toggle = event.currentTarget; // Elemen submenu-toggle yang diklik
    var caret = toggle.querySelector('.caret');
    var content = toggle.nextElementSibling; // Submenu-content terkait

    // Toggle display submenu
    if (content.style.display === "block") {
        content.style.display = "none"; // Tutup submenu
        caret.classList.remove("flip"); // Kembalikan posisi panah
        toggle.classList.remove("active"); // Hapus kelas active pada submenu-toggle
    } else {
        content.style.display = "block"; // Buka submenu
        caret.classList.add("flip"); // Balikkan posisi panah
        toggle.classList.add("active"); // Tambahkan kelas active pada submenu-toggle
    }
}

    // Fungsi untuk menandai menu aktif
    document.addEventListener("DOMContentLoaded", function () {
        const currentPath = window.location.pathname; // Ambil path URL saat ini
        const menuLinks = document.querySelectorAll("#menu a");

        menuLinks.forEach(link => {
            // Cek apakah path URL sesuai dengan href elemen
            if (link.getAttribute("href") === currentPath) {
                link.classList.add("active");

                // Jika link berada di submenu, buka parentnya
                const submenu = link.closest(".submenu");
                if (submenu) {
                    submenu.classList.add("open");
                }
            }
        });
    });

function confirmDownload(event) {
        // Tampilkan dialog konfirmasi
        const userConfirmed = confirm("Apakah Anda yakin ingin mengunduh file CSV?");

        // Jika pengguna menolak, batalkan aksi
        if (!userConfirmed) {
            event.preventDefault(); // Batalkan navigasi
        }

        return userConfirmed; // Lanjutkan hanya jika pengguna mengonfirmasi
    }

