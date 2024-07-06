const { createCanvas, loadImage, registerFont } = require('canvas');
const { format, parse } = require('date-fns');

function alignText(context, text, x, y, maxWidth, align, fontSize, fontColor, thickness) {
    context.font = `${fontSize}px Helvetica`;
    context.fillStyle = fontColor;
    context.textAlign = align;
    context.fillText(text, x, y, maxWidth);
}

async function createStudyCertificate(templatePath, outputPath, name, course, date) {
    try {
        const template = await loadImage(templatePath);
        const canvas = createCanvas(template.width, template.height);
        const context = canvas.getContext('2d');

        context.drawImage(template, 0, 0);

        // Define font properties
        const fontSize = 24; // Adjust size as needed
        const fontColor = '#000000'; // Black color
        const thickness = 2; // This will not be used directly in canvas

        // Calculate positions and alignment dynamically if needed
        // Add name
        const nameY = 355; // Adjust this position as needed
        alignText(context, name, template.width / 2, nameY, template.width, 'center', fontSize, fontColor, thickness);

        // Add course
        const courseY = 470; // Adjust this position as needed
        alignText(context, course, template.width / 2, courseY, template.width, 'center', fontSize, fontColor, thickness);

        // Add date
        const dateObj = parse(date, 'MMMM dd, yyyy', new Date());
        const dayY = 540; // Adjust this position as needed
        const monthY = dayY;
        const yearY = dayY;
        alignText(context, `${dateObj.getDate()}`, 270, dayY, 139, 'center', 20, fontColor, thickness);
        alignText(context, format(dateObj, 'MMMM'), 490, monthY, 130, 'center', 20, fontColor, thickness);
        alignText(context, `${dateObj.getFullYear()}`, 720, yearY, 135, 'center', 20, fontColor, thickness);

        // Add a unique identifier (for NFT purposes)
        const uniqueId = Math.floor(Math.random() * (99999 - 10000 + 1)) + 10000;
        alignText(context, `NFT ID: ${uniqueId}`, template.width - 10, 50, 200, 'right', 16, fontColor, thickness);

        // Save the result
        const buffer = canvas.toBuffer('image/png');
        require('fs').writeFileSync(outputPath, buffer);

        console.log(`Certificate created with NFT ID: ${uniqueId}`);
    } catch (error) {
        console.error('Failed to create certificate:', error);
    }
}

// Example usage
createStudyCertificate('./path_to_template.png', './output_path.png', 'John Doe', 'Advanced Node.js', 'October 15, 2021');
