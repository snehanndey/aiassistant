document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('interview-form');
    const resultContainer = document.getElementById('result-container');

    form.addEventListener('submit', async (event) => {
        event.preventDefault();

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

            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            const data = await response.json();
            displayResults(data);

        } catch (error) {
            console.error('Error:', error);
            resultContainer.innerHTML = `<p class="error">An error occurred. Please check the console for details.</p>`;
        }
    });

    function displayResults(data) {
        if (!data.questions || data.questions.length === 0) {
            resultContainer.innerHTML = '<p>No questions were generated.</p>';
            return;
        }

        let html = '<h2>Generated Questions</h2><ul>';
        data.questions.forEach(q => {
            html += `<li><strong>${q.question}</strong> (Topic: ${q.topic})</li>`;
        });
        html += '</ul>';

        resultContainer.innerHTML = html;
    }
});