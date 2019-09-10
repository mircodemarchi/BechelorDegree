function write3Video(video1, video2, video3, filevideoname)
%WRITE3VIDEO Salva in un solo video, tre video di frame
%  INPUT
%   video1: Primo video di frame da salvare;
%   video2: Secondo video di frame da salvare;
%   video3: Terzo video di frame da salvare;
%   filevideoname: Nome del file in cui si vuole salvare il risultato;
%  OUTPUT
%   void
%
%  Condizione: la lunghezza dei primi 2 video di frame in input deve essere 
%              uguale! Il terzo video può anche essere diverso.
%

% Creo un nuovo video di formato mp4 con il nome dato
vidObj = VideoWriter(filevideoname, 'MPEG-4');
open(vidObj);

% Scorro i primi due video di frame, li mostro su un plot di griglia 3x1, 
% e salvo l'immagine del plot sul video creato.
figure('Renderer', 'painters', 'Position', [400 200 500 700]);
for i=1:size(video1,4)
    subplot(311); imshow(video1(:,:,:,i)); title("Video Originale");
    subplot(312); imshow(video2(:,:,:,i)); title("Video Stabilizzato");
    currFrame = getframe(gcf);
    writeVideo(vidObj,currFrame);
end

% Scorro l'ultimo video di frame, lo mostro sul subplot che è rimasto del 
% plot di griglia 3x1 creato precedentemente, e salvo l'immagine del plot 
% sul video creato.
for i=1:size(video3,4)
    subplot(313); imshow(video3(:,:,:,i)); title("Video Stabilizzato con Selezione");
    currFrame = getframe(gcf);
    writeVideo(vidObj,currFrame);
end

close(vidObj);

end

