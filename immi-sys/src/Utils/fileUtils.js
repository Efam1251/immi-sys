// Validate and prepare file
  export function validateAndPrepareFile(file, reference, documentId) {
    if (!file) return { error: "No file selected" };
    if (!documentId) return { error: "Document type must be selected before uploading." };
  
    const fileExtension = file.name.split(".").pop().toLowerCase();
    const allowedExtensions = ["pdf", "jpg", "jpeg", "png", "docx"];
  
    if (!allowedExtensions.includes(fileExtension)) {
      return { error: "Unsupported file type selected." };
    }
  
    const structuredFileName = `${reference}-${documentId}.${fileExtension}`;
    return { file, fileName: structuredFileName };
  };
  
  // Process file change
  export const processFileChange = (event, baseName, updateCallback) => {
    const selectedFile = event.target.files[0];
    
    if (selectedFile) {
      const fileExtension = selectedFile.name.split('.').pop();
      const structuredFileName = `${baseName}.${fileExtension}`;
  
      updateCallback({
        file: selectedFile,
        fileName: structuredFileName,
        location: structuredFileName,
      });
    } else {
      console.log("No file selected.");
    }
  };