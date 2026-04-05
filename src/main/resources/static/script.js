document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('interview-form');
    const resultContainer = document.getElementById('result-container');
    const submitButton = form.querySelector('button[type="submit"]');

    form.addEventListener('submit', async (event) => {
        event.preventDefault();

        // Disable button and show processing message
        submitButton.disabled = true;
        submitButton.textContent = 'Generating...';
        resultContainer.innerHTML = `
            <div class="processing-container">
                <div class="loader"></div>
                <p>Your request is being processed. Please wait...</p>
            </div>
        `;

        const startTime = performance.now();

        const formData = new FormData(form);
        const request = {
            jobRole: formData.get('jobRole'),
            experienceLevel: formData.get('experienceLevel'),
            numberOfQuestions: parseInt(formData.get('numberOfQuestions'), 10)
        };

        try {
            const response = await fetch('/api/v1/interview/generate', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(request),
            });

            const endTime = performance.now();
            const duration = ((endTime - startTime) / 1000).toFixed(2);

            if (!response.ok) {
                const errorData = await response.json().catch(() => ({ message: 'Could not parse error response.' }));
                throw new Error(errorData.message || `HTTP error! status: ${response.status}`);
            }

            const data = await response.json();
            displayResults(data, duration);

        } catch (error) {
            console.error('Error:', error);
            resultContainer.innerHTML = `<p class="error">An error occurred: ${error.message}</p>`;
        } finally {
            // Re-enable button
            submitButton.disabled = false;
            submitButton.textContent = 'Generate Questions';
        }
    });

    function displayResults(data, duration) {
        if (!data.questions || data.questions.length === 0) {
            resultContainer.innerHTML = '<p>No questions were generated.</p>';
            return;
        }

        let html = `
            <div class="results-header">
                <h2>Generated Questions</h2>
                <span class="duration-badge">(Completed in ${duration} seconds)</span>
            </div>
        `;
        data.questions.forEach(q => {
            html += `
                <details class="question-item">
                    <summary>${q.question}</summary>
                    <p>${q.suggestedAnswer}</p>
                </details>
            `;
        });

        resultContainer.innerHTML = html;
    }
});