function write2Video(frames, new, filevideoname)
%WRITE2VIDEO Salva in un solo video, due video di frame
%  INPUT
%   frames: Primo video di frame da salvare;
%   new: Secondo video di frame da salvare;
%   filevideoname: Nome del file in cui si vuole salvare il risultato;
%  OUTPUT
%   void
%
%  Condizione: la lunghezza dei due video di frame  in input deve essere 
%              uguale!
%

% Creo un nuovo video di formato mp4 con il nome dato
vidObj = VideoWriter(filevideoname, 'MPEG-4');
open(vidObj);

% Scorro entrambi i video di frame, li mostro su un plot di griglia 1x2, 
% e salvo l'immagine del plot sul video creato.
figure('Renderer', 'painters', 'Position', [300 500 900 300]);
for i=1:size(frames,4)
    subplot(121); imshow(frames(:,:,:,i)); title("Video Originale");
    subplot(122); imshow(new(:,:,:,i)); title("Video Stabilizzato");
    currFrame = getframe(gcf);
    writeVideo(vidObj,currFrame);
end

close(vidObj);

end

