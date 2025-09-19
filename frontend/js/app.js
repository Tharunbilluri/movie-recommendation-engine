class MovieRecommendationApp {
    constructor() {
        this.baseUrl = 'http://localhost:8080/api';
        this.currentUser = 1;
        this.currentSection = 'popular';
        this.selectedMovieId = null;
        this.selectedRating = 0;
        
        this.initializeEventListeners();
        this.loadRecommendations();
    }

    initializeEventListeners() {
        // Navigation buttons
        document.querySelectorAll('.nav-btn').forEach(btn => {
            btn.addEventListener('click', (e) => {
                this.switchSection(e.target.dataset.section);
            });
        });

        // User selection
        document.getElementById('userSelect').addEventListener('change', (e) => {
            this.currentUser = parseInt(e.target.value);
            if (this.currentSection === 'collaborative') {
                this.loadRecommendations();
            }
        });

        // Genre selection
        document.getElementById('genreSelect').addEventListener('change', () => {
            if (this.currentSection === 'content') {
                this.loadRecommendations();
            }
        });

        // Modal controls
        document.getElementById('closeModal').addEventListener('click', () => {
            this.closeModal();
        });

        window.addEventListener('click', (e) => {
            if (e.target.id === 'movieModal') {
                this.closeModal();
            }
        });

        // Star rating
        document.querySelectorAll('#starRating i').forEach(star => {
            star.addEventListener('click', (e) => {
                this.setRating(parseInt(e.target.dataset.rating));
            });

            star.addEventListener('mouseenter', (e) => {
                this.highlightStars(parseInt(e.target.dataset.rating));
            });
        });

        document.getElementById('starRating').addEventListener('mouseleave', () => {
            this.highlightStars(this.selectedRating);
        });

        // Submit rating
        document.getElementById('submitRating').addEventListener('click', () => {
            this.submitRating();
        });
    }

    switchSection(section) {
        // Update active navigation
        document.querySelectorAll('.nav-btn').forEach(btn => {
            btn.classList.remove('active');
        });
        document.querySelector(`[data-section="${section}"]`).classList.add('active');

        this.currentSection = section;

        // Show/hide genre filter
        const genreFilter = document.getElementById('genreFilter');
        if (section === 'content') {
            genreFilter.style.display = 'block';
        } else {
            genreFilter.style.display = 'none';
        }

        // Update section title and description
        this.updateSectionHeader(section);

        // Load recommendations
        this.loadRecommendations();
    }

    updateSectionHeader(section) {
        const title = document.getElementById('sectionTitle');
        const description = document.getElementById('sectionDescription');

        switch (section) {
            case 'popular':
                title.textContent = 'Popular Movies';
                description.textContent = 'Discover the most highly-rated movies';
                break;
            case 'content':
                title.textContent = 'Movies by Genre';
                description.textContent = 'Find movies that match your favorite genre';
                break;
            case 'collaborative':
                title.textContent = 'Recommended for You';
                description.textContent = 'Personalized recommendations based on similar users';
                break;
        }
    }

    async loadRecommendations() {
        this.showLoading(true);

        try {
            let url;
            switch (this.currentSection) {
                case 'popular':
                    url = `${this.baseUrl}/recommendations/popular?limit=8`;
                    break;
                case 'content':
                    const genre = document.getElementById('genreSelect').value;
                    url = `${this.baseUrl}/recommendations/content?genre=${genre}&limit=8`;
                    break;
                case 'collaborative':
                    url = `${this.baseUrl}/recommendations/collaborative?userId=${this.currentUser}&limit=8`;
                    break;
            }

            const response = await fetch(url);
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            
            const movies = await response.json();
            this.displayMovies(movies);
        } catch (error) {
            console.error('Error loading recommendations:', error);
            this.showError('Failed to load recommendations. Please try again.');
        } finally {
            this.showLoading(false);
        }
    }

    displayMovies(movies) {
        const grid = document.getElementById('moviesGrid');
        
        if (movies.length === 0) {
            grid.innerHTML = `
                <div style="grid-column: 1 / -1; text-align: center; padding: 3rem;">
                    <i class="fas fa-film" style="font-size: 3rem; color: #ccc; margin-bottom: 1rem;"></i>
                    <h3 style="color: #666;">No recommendations found</h3>
                    <p style="color: #999;">Try selecting a different genre or user.</p>
                </div>
            `;
            return;
        }

        grid.innerHTML = movies.map(movie => `
            <div class="movie-card" onclick="app.openMovieModal(${movie.id})">
                <div class="movie-poster">
                    <img src="${movie.imageUrl}" alt="${movie.title}" loading="lazy">
                </div>
                <div class="movie-info">
                    <h3 class="movie-title">${movie.title}</h3>
                    <div class="movie-meta">
                        <span class="year">${movie.year}</span>
                        <span class="genre">${movie.genre}</span>
                        <span class="rating">
                            <i class="fas fa-star"></i>
                            ${movie.rating}
                        </span>
                    </div>
                    <p class="description">${movie.description}</p>
                </div>
            </div>
        `).join('');
    }

    async openMovieModal(movieId) {
        try {
            const response = await fetch(`${this.baseUrl}/movies`);
            const movies = await response.json();
            const movie = movies.find(m => m.id === movieId);

            if (!movie) {
                throw new Error('Movie not found');
            }

            this.selectedMovieId = movieId;
            this.selectedRating = 0;

            // Populate modal
            document.getElementById('modalPoster').src = movie.imageUrl;
            document.getElementById('modalTitle').textContent = movie.title;
            document.getElementById('modalYear').textContent = movie.year;
            document.getElementById('modalGenre').textContent = movie.genre;
            document.getElementById('modalRating').textContent = movie.rating;
            document.getElementById('modalDescription').textContent = movie.description;

            // Reset star rating
            this.highlightStars(0);

            // Show modal
            document.getElementById('movieModal').style.display = 'block';
        } catch (error) {
            console.error('Error opening movie modal:', error);
            this.showError('Failed to load movie details.');
        }
    }

    closeModal() {
        document.getElementById('movieModal').style.display = 'none';
        this.selectedMovieId = null;
        this.selectedRating = 0;
    }

    setRating(rating) {
        this.selectedRating = rating;
        this.highlightStars(rating);
    }

    highlightStars(rating) {
        document.querySelectorAll('#starRating i').forEach((star, index) => {
            if (index < rating) {
                star.classList.add('active');
            } else {
                star.classList.remove('active');
            }
        });
    }

    async submitRating() {
        if (!this.selectedMovieId || this.selectedRating === 0) {
            this.showError('Please select a rating first.');
            return;
        }

        const submitBtn = document.getElementById('submitRating');
        submitBtn.disabled = true;
        submitBtn.textContent = 'Submitting...';

        try {
            const response = await fetch(`${this.baseUrl}/rate`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    userId: this.currentUser,
                    movieId: this.selectedMovieId,
                    rating: this.selectedRating
                })
            });

            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            this.showToast('Rating submitted successfully!');
            this.closeModal();

            // Refresh recommendations if in collaborative mode
            if (this.currentSection === 'collaborative') {
                setTimeout(() => {
                    this.loadRecommendations();
                }, 1000);
            }
        } catch (error) {
            console.error('Error submitting rating:', error);
            this.showError('Failed to submit rating. Please try again.');
        } finally {
            submitBtn.disabled = false;
            submitBtn.textContent = 'Submit Rating';
        }
    }

    showLoading(show) {
        const loading = document.getElementById('loading');
        const grid = document.getElementById('moviesGrid');
        
        if (show) {
            loading.style.display = 'block';
            grid.style.opacity = '0.5';
        } else {
            loading.style.display = 'none';
            grid.style.opacity = '1';
        }
    }

    showToast(message) {
        const toast = document.getElementById('toast');
        const toastMessage = document.getElementById('toastMessage');
        
        toastMessage.textContent = message;
        toast.classList.add('show');
        
        setTimeout(() => {
            toast.classList.remove('show');
        }, 3000);
    }

    showError(message) {
        // Create a temporary error toast
        const toast = document.getElementById('toast');
        const toastMessage = document.getElementById('toastMessage');
        const toastContent = toast.querySelector('.toast-content');
        
        // Temporarily change toast style for error
        toast.style.background = '#dc3545';
        toastContent.querySelector('i').className = 'fas fa-exclamation-circle';
        
        toastMessage.textContent = message;
        toast.classList.add('show');
        
        setTimeout(() => {
            toast.classList.remove('show');
            // Reset toast style
            setTimeout(() => {
                toast.style.background = '#28a745';
                toastContent.querySelector('i').className = 'fas fa-check-circle';
            }, 300);
        }, 4000);
    }
}

// Initialize the application when the DOM is loaded
document.addEventListener('DOMContentLoaded', () => {
    window.app = new MovieRecommendationApp();
});

// Handle server connection errors gracefully
window.addEventListener('unhandledrejection', (event) => {
    console.error('Unhandled promise rejection:', event.reason);
    if (event.reason.message && event.reason.message.includes('fetch')) {
        if (window.app) {
            window.app.showError('Unable to connect to the server. Please make sure the Java backend is running.');
        }
    }
});
