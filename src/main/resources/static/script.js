document.addEventListener('DOMContentLoaded', () => {
    const generalForm = document.getElementById('general-interview-form');
    const resumeForm = document.getElementById('resume-interview-form');
    const resultContainer = document.getElementById('result-container');

    // Handle the General Interview Form submission
    generalForm.addEventListener('submit', async (event) => {
        event.preventDefault();
        const submitButton = generalForm.querySelector('button[type="submit"]');
        const formData = new FormData(generalForm);
        const request = {
            jobRole: formData.get('jobRole'),
            experienceLevel: formData.get('experienceLevel'),
            numberOfQuestions: parseInt(formData.get('numberOfQuestions'), 10)
        };

        await handleRequest(
            '/api/v1/interview/generate',
            {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(request)
            },
            submitButton
        );
    });

    // Handle the Resume Interview Form submission
    resumeForm.addEventListener('submit', async (event) => {
        event.preventDefault();
        const submitButton = resumeForm.querySelector('button[type="submit"]');
        const formData = new FormData(resumeForm);

        await handleRequest(
            '/api/v1/resume-interview/generate',
            {
                method: 'POST',
                body: formData // No 'Content-Type' header needed; browser sets it for FormData
            },
            submitButton
        );
    });

    // Generic function to handle fetch, feedback, and display
    async function handleRequest(url, options, submitButton) {
        const originalButtonText = submitButton.textContent;
        submitButton.disabled = true;
        submitButton.textContent = 'Generating...';
        resultContainer.innerHTML = `
            <div class="processing-container">
                <div class="loader"></div>
                <p>Your request is being processed. Please wait...</p>
            </div>
        `;

        const startTime = performance.now();

        try {
            const response = await fetch(url, options);
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
            submitButton.disabled = false;
            submitButton.textContent = originalButtonText;
        }
    }

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
